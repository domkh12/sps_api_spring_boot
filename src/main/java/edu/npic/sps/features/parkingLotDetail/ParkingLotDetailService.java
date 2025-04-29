package edu.npic.sps.features.parkingLotDetail;

import edu.npic.sps.features.parkingLotDetail.dto.ParkingDetailResponse;
import org.springframework.data.domain.Page;

public interface ParkingLotDetailService {

    ParkingDetailResponse getParkingDetailByUuid(String uuid);

    Page<ParkingDetailResponse> findAll(int pageNo, int pageSize);
}
