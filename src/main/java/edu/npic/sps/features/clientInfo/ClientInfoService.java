package edu.npic.sps.features.clientInfo;

import edu.npic.sps.features.clientInfo.dto.ClientInfoResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

public interface ClientInfoService {
    Page<ClientInfoResponse> getByUserId(Integer userId, int pageNo, int pageSize);

    void storeClientInfo(HttpServletRequest request, Integer userId, String action, String loginType);

    String extractClientIp(HttpServletRequest request);
}

