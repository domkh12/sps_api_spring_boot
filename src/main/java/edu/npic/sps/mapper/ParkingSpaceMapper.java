package edu.npic.sps.mapper;

import edu.npic.sps.domain.ParkingSpace;
import edu.npic.sps.features.parkingSpace.dto.CreateParking;
import edu.npic.sps.features.parkingSpace.dto.LabelResponse;
import edu.npic.sps.features.parkingSpace.dto.ParkingSpaceResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ParkingSpaceMapper {

    ParkingSpace fromCreateParking(CreateParking createParking);

    ParkingSpaceResponse toParkingSpaceResponse(ParkingSpace parkingSpace);

    List<LabelResponse> toLabelResponse(List<ParkingSpace> parkingSpace);

    List<ParkingSpaceResponse> toParkingListResponse(List<ParkingSpace> parkingSpaceList);

}
