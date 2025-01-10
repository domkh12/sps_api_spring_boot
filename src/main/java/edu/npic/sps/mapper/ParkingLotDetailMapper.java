package edu.npic.sps.mapper;

import edu.npic.sps.domain.ParkingLotDetail;
import edu.npic.sps.features.parkingLotDetail.dto.ParkingDetailResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ParkingLotDetailMapper {

    ParkingDetailResponse toParkingDetailResponse(ParkingLotDetail parkingLotDetail);
}
