package edu.npic.sps.features.role;

import edu.npic.sps.domain.Role;
import edu.npic.sps.domain.User;
import edu.npic.sps.features.role.dto.RoleResponse;
import edu.npic.sps.mapper.RoleMapper;
import edu.npic.sps.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final AuthUtil authUtil;

    @Override
    public List<RoleResponse> findAll() {

        List<Role> roles = roleRepository.findAll();

        return roleMapper.toRoleResponse(roles);
    }
}
