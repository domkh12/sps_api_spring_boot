package edu.npic.sps.mapper;

import edu.npic.sps.domain.ParkingLot;
import edu.npic.sps.domain.ParkingLotDetail;
import edu.npic.sps.features.parkingLot.dto.CreateCarParking;
import edu.npic.sps.features.parkingLot.dto.ParkingLotResponse;
import edu.npic.sps.features.parkingLot.dto.ParkingSlotDetailResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParkingLotMapper {

    ParkingSlotDetailResponse toParkingSlotDetailResponse(ParkingLotDetail parkingLotDetail);

    ParkingLotResponse toParkingSlotResponse(ParkingLot parkingLot);

    ParkingLotDetail fromCreateCarParking(CreateCarParking createCarParking);

}
