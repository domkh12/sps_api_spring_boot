package edu.npic.sps.mapper;

import edu.npic.sps.domain.Role;
import edu.npic.sps.features.role.dto.RoleResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    List<RoleResponse> toRoleResponse(List<Role> roles);
}
