package edu.npic.sps.mapper;

import edu.npic.sps.domain.ParkingLot;
import edu.npic.sps.domain.ParkingLotDetail;
import edu.npic.sps.features.parkingLot.dto.CreateCarParking;
import edu.npic.sps.features.parkingLot.dto.ParkingLotRequest;
import edu.npic.sps.features.parkingLot.dto.ParkingLotResponse;
import edu.npic.sps.features.parkingLot.dto.ParkingSlotDetailResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ParkingLotMapper {

    ParkingSlotDetailResponse toParkingSlotDetailResponse(ParkingLotDetail parkingLotDetail);

    ParkingLotResponse toParkingSlotResponse(ParkingLot parkingLot);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void fromParkingLotRequest(ParkingLotRequest parkingLotRequest, @MappingTarget ParkingLot parkingLot);

}
