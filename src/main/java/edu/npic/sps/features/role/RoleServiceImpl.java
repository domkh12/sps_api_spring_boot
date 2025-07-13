package edu.npic.sps.features.role;

import edu.npic.sps.domain.Role;
import edu.npic.sps.domain.User;
import edu.npic.sps.features.role.dto.RoleRequest;
import edu.npic.sps.features.role.dto.RoleResponse;
import edu.npic.sps.mapper.RoleMapper;
import edu.npic.sps.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final AuthUtil authUtil;


    @Override
    public void deleteRole(String uuid) {
        Role role = roleRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!")
        );

        roleRepository.deleteByUuid(role.getUuid());
    }

    @Override
    public RoleResponse updateRole(String uuid, RoleRequest roleRequest) {
        Role role = roleRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!")
        );

        roleMapper.updateRoleFromRequest(roleRequest, role);
        Role updatedRole = roleRepository.save(role);

        return roleMapper.toRoleResponse(updatedRole);
    }

    @Override
    public RoleResponse createRole(RoleRequest roleRequest) {
        Role role = roleMapper.fromRoleRequest(roleRequest);
        role.setUuid(UUID.randomUUID().toString());
        role.setCreatedAt(LocalDateTime.now());
        Role savedRole = roleRepository.save(role);

        return roleMapper.toRoleResponse(savedRole);
    }

    @Override
    public List<RoleResponse> findAll() {

        List<Role> roles = roleRepository.findAll();

        return roles.stream().map(roleMapper::toRoleResponse).toList();
    }
}
