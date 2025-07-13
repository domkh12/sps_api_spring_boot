package edu.npic.sps.mapper;

import edu.npic.sps.domain.Role;
import edu.npic.sps.features.role.dto.RoleRequest;
import edu.npic.sps.features.role.dto.RoleResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRoleFromRequest(RoleRequest roleRequest, @MappingTarget Role role);

    Role fromRoleRequest(RoleRequest roleRequest);

    RoleResponse toRoleResponse(Role roles);
}
