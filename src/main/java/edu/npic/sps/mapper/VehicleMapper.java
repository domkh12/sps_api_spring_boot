package edu.npic.sps.mapper;

import edu.npic.sps.domain.Vehicle;
import edu.npic.sps.features.vehicle.dto.CreateVehicle;
import edu.npic.sps.features.vehicle.dto.VehicleRequest;
import edu.npic.sps.features.vehicle.dto.VehicleResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromVehicleRequest(VehicleRequest vehicleRequest, @MappingTarget Vehicle vehicle);

    Vehicle fromCreateVehicle(CreateVehicle createVehicle);

    VehicleResponse toVehicleResponse(Vehicle vehicle);

}
