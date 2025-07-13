package edu.npic.sps.features.role;

import edu.npic.sps.features.role.dto.RoleRequest;
import edu.npic.sps.features.role.dto.RoleResponse;

import java.util.List;

public interface RoleService {

    void deleteRole(String uuid);

    RoleResponse updateRole(String uuid, RoleRequest roleRequest);

    RoleResponse createRole(RoleRequest roleRequest);

    List<RoleResponse> findAll();
}
