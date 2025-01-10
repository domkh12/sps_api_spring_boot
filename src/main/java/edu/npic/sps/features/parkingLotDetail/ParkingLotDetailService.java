package edu.npic.sps.features.parkingLotDetail;

import edu.npic.sps.features.parkingLotDetail.dto.ParkingDetailResponse;

public interface ParkingLotDetailService {

    void createCarParking(String numberPlate,String locationId, String slotId);

    ParkingDetailResponse getParkingDetailByUuid(String uuid);

}
