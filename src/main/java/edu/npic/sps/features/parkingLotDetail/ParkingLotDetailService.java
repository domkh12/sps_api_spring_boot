package edu.npic.sps.features.parkingLotDetail;

import edu.npic.sps.features.parkingLotDetail.dto.ParkingDetailResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface ParkingLotDetailService {

    Page<ParkingDetailResponse> filter(int pageNo, int pageSize, String keywords, LocalDateTime dateFrom, LocalDateTime dateTo);

    ParkingDetailResponse getParkingDetailByUuid(String uuid);

    Page<ParkingDetailResponse> findAll(int pageNo, int pageSize);
}
