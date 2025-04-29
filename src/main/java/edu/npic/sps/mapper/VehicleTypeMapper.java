package edu.npic.sps.mapper;

import edu.npic.sps.domain.Vehicle;
import edu.npic.sps.domain.VehicleType;
import edu.npic.sps.features.vehicletype.dto.UpdateRequest;
import edu.npic.sps.features.vehicletype.dto.VehicleTypeResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "Spring")
public interface VehicleTypeMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromUpdateRequest(UpdateRequest updateRequest, @MappingTarget VehicleType vehicleType);

    VehicleTypeResponse toVehicleTypeResponse(VehicleType vehicleType);

    List<VehicleTypeResponse> toListVehicleTypeResponse(List<VehicleType> vehicleTypes);
}
