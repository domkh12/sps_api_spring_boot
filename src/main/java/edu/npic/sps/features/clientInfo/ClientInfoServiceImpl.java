package edu.npic.sps.features.clientInfo;

import edu.npic.sps.domain.ClientInfo;
import edu.npic.sps.features.clientInfo.dto.ClientInfoResponse;
import edu.npic.sps.mapper.ClientInfoMapper;
import edu.npic.sps.util.RegexUtil;
import edu.npic.sps.util.Util;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientInfoServiceImpl implements ClientInfoService {

    private final ClientInfoRepository clientInfoRepository;
    private final ClientInfoMapper clientInfoMapper;

    @Override
    public Page<ClientInfoResponse> getByUserId(Integer userId, int pageNo, int pageSize) {

        if (pageNo < 1 || pageSize < 1){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page number or page size must greater than 0!"
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<ClientInfo> clientInfos = clientInfoRepository.findByUserId(userId, pageRequest);

        return clientInfos.map(clientInfoMapper::toClientInfoResponse) ;
    }



    /**
     * Extracts and validates client IP address from request
     */
    public String extractClientIp(HttpServletRequest request) {
        String[] headerNames = {
                "X-Forwarded-For",
                "X-Real-IP",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_CLIENT_IP"
        };

        for (String headerName : headerNames) {
            String ip = request.getHeader(headerName);
            if (isValidIp(ip)) {
                // Handle comma-separated IPs (take the first valid one)
                if (ip.contains(",")) {
                    String[] ips = ip.split(",");
                    for (String singleIp : ips) {
                        String trimmedIp = singleIp.trim();
                        if (isValidIp(trimmedIp) && !isPrivateIp(trimmedIp)) {
                            return sanitizeIp(trimmedIp);
                        }
                    }
                }
                return sanitizeIp(ip);
            }
        }

        // Fallback to remote address
        String remoteAddr = request.getRemoteAddr();
        return isValidIp(remoteAddr) ? sanitizeIp(remoteAddr) : null;
    }

    /**
     * Validates IP address format
     */
    private boolean isValidIp(String ip) {
        if (ip == null || ip.trim().isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            return false;
        }

        ip = ip.trim();
        return RegexUtil.IPV4_PATTERN.matcher(ip).matches() || RegexUtil.IPV6_PATTERN.matcher(ip).matches();
    }

    /**
     * Checks if IP is in private/internal range
     */
    private boolean isPrivateIp(String ip) {
        if (ip == null) return true;

        String lowerIp = ip.toLowerCase();
        return RegexUtil.PRIVATE_IP_PREFIXES.stream().anyMatch(lowerIp::startsWith);
    }

    /**
     * Sanitizes IP address (removes any potential malicious content)
     */
    private String sanitizeIp(String ip) {
        if (ip == null) return null;

        // Remove any non-IP characters (security measure)
        ip = ip.replaceAll("[^0-9a-fA-F:.%]", "");

        // Limit length to prevent storage issues
        if (ip.length() > 45) { // Max IPv6 length
            log.warn("IP address too long, truncating: {}", ip);
            ip = ip.substring(0, 45);
        }

        return ip;
    }

    /**
     * Anonymizes IP address for privacy compliance (GDPR, etc.)
     */
    public String anonymizeIp(String ip) {
        if (ip == null || ip.isEmpty()) {
            return ip;
        }

        try {
            if (ip.contains(".")) {
                // IPv4 - zero out last octet
                String[] parts = ip.split("\\.");
                if (parts.length == 4) {
                    return parts[0] + "." + parts[1] + "." + parts[2] + ".0";
                }
            } else if (ip.contains(":")) {
                // IPv6 - zero out last 64 bits
                String[] parts = ip.split(":");
                if (parts.length >= 4) {
                    StringBuilder anonymized = new StringBuilder();
                    for (int i = 0; i < Math.min(4, parts.length); i++) {
                        if (i > 0) anonymized.append(":");
                        anonymized.append(parts[i]);
                    }
                    anonymized.append("::");
                    return anonymized.toString();
                }
            }
        } catch (Exception e) {
            log.error("Error anonymizing IP: {}", ip, e);
        }

        return ip;
    }

    /**
     * Extracts User Agent from request
     */
    public String extractUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return sanitizeUserAgent(userAgent);
    }

    /**
     * Sanitizes User Agent string
     */
    private String sanitizeUserAgent(String userAgent) {
        if (userAgent == null || userAgent.trim().isEmpty()) {
            return "Unknown";
        }

        // Remove potential malicious content and limit length
        userAgent = userAgent.replaceAll("[<>\"'&]", ""); // Remove HTML/XML chars
        userAgent = userAgent.trim();

        // Limit length to prevent storage issues
        if (userAgent.length() > 500) {
            log.warn("User Agent too long, truncating: {}", userAgent.substring(0, 100) + "...");
            userAgent = userAgent.substring(0, 500);
        }

        return userAgent;
    }

    @Transactional
    public void storeClientInfo(HttpServletRequest request, Integer userId, String action, String loginType) {
        String rawIp = extractClientIp(request);
        String userAgent = extractUserAgent(request);
        if (rawIp == null) {
            log.warn("Could not extract valid IP address from request");
            return;
        }

        try {
              ClientInfo clientInfo = new ClientInfo();
              clientInfo.setUserId(userId);
              clientInfo.setAction(action);
              clientInfo.setCreatedAt(LocalDateTime.now());
              clientInfo.setAnonymizedIp(anonymizeIp(rawIp));
              clientInfo.setIpAddress(rawIp);
              clientInfo.setLoginType(loginType);
              clientInfo.setDeviceTye(Util.getDeviceTypeName(userAgent));
              clientInfo.setOperatingSystem(Util.getOsName(userAgent));
              clientInfo.setBrowser(Util.getBrowserName(userAgent));
              clientInfo.setRawUserAgent(userAgent);
              clientInfoRepository.save(clientInfo);

        } catch (Exception e) {
            log.error("Error storing client information for user {} action {}", userId, action, e);
        }
    }


}
