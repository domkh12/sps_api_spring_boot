package edu.npic.sps.features.parkingLot;

import edu.npic.sps.features.parkingLot.dto.CreateParkingLot;
import edu.npic.sps.features.parkingLot.dto.ParkingLotRequest;
import edu.npic.sps.features.parkingLot.dto.ParkingLotResponse;
import edu.npic.sps.features.parkingLot.dto.ParkingSlotDetailResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ParkingLotService {

    Page<ParkingLotResponse> findAll(int pageNo, int pageSize);

//    void createCarParking(CreateCarParking createCarParking);

    ParkingSlotDetailResponse findByUuid(String uuid);

    List<ParkingLotResponse> create(CreateParkingLot createParkingLot);

    List<ParkingLotResponse> update(String uuid, ParkingLotRequest parkingLotRequest);

    ParkingLotResponse findParkingLotByUuid(String uuid);
}
