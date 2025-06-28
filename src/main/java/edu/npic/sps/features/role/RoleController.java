package edu.npic.sps.features.role;

import edu.npic.sps.features.role.dto.RoleResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
@Tag(name = "Role Management", description = "APIs for managing roles")
public class RoleController {

    private final RoleService roleService;

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<RoleResponse> findAll(){
        return roleService.findAll();
    }

}
