package edu.npic.sps.features.parkingLot;

import edu.npic.sps.features.parkingLot.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ParkingLotService {

    List<ParkingLotResponse> createMultipleParkingLot(CreateMultipleSlot createMultipleSlot);

    Page<ParkingLotResponse> findAll(int pageNo, int pageSize);

//    void createCarParking(CreateCarParking createCarParking);

    ParkingSlotDetailResponse findByUuid(String uuid);

    List<ParkingLotResponse> create(CreateParkingLot createParkingLot);

    List<ParkingLotResponse> update(String uuid, ParkingLotRequest parkingLotRequest);

    ParkingLotResponse findParkingLotByUuid(String uuid);
}
