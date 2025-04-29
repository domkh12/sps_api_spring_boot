package edu.npic.sps.mapper;

import edu.npic.sps.domain.ParkingSpace;
import edu.npic.sps.features.parkingSpace.dto.CreateParkingSpace;
import edu.npic.sps.features.parkingSpace.dto.LabelResponse;
import edu.npic.sps.features.parkingSpace.dto.ParkingSpaceRequest;
import edu.npic.sps.features.parkingSpace.dto.ParkingSpaceResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParkingSpaceMapper {

    ParkingSpace fromCreateParking(CreateParkingSpace createParkingSpace);

    ParkingSpaceResponse toParkingSpaceResponse(ParkingSpace parkingSpace);

    List<LabelResponse> toLabelResponse(List<ParkingSpace> parkingSpace);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromParkingSpaceRequest(ParkingSpaceRequest parkingSpaceRequest, @MappingTarget ParkingSpace parkingSpace);

}
