package edu.npic.sps.features.role;

import edu.npic.sps.features.role.dto.RoleResponse;

import java.util.List;

public interface RoleService {
    List<RoleResponse> findAll();
}
