package edu.npic.sps.features.clientInfo;

import edu.npic.sps.features.clientInfo.dto.ClientInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/client-info")
@RequiredArgsConstructor
public class ClientInfoController {

    private final ClientInfoService clientInfoService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    Page<ClientInfoResponse> getByUserId(
            @PathVariable Integer userId,
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "20") int pageSize){
        return clientInfoService.getByUserId(userId, pageNo, pageSize);
    }

}
