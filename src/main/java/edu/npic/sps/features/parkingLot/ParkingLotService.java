package edu.npic.sps.features.parkingLot;

import edu.npic.sps.features.parkingLot.dto.ParkingSlotDetailResponse;
import org.springframework.data.domain.Page;

public interface ParkingLotService {

    Page<ParkingSlotDetailResponse> findAll(int pageNo, int pageSize);

//    void createCarParking(CreateCarParking createCarParking);

    ParkingSlotDetailResponse findByUuid(String uuid);
}
