package edu.npic.sps.features.parkingSpace.dto;

import edu.npic.sps.domain.Site;
import edu.npic.sps.features.parkingLot.dto.ParkingLotResponse;
import edu.npic.sps.features.site.dto.SiteResponse;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ParkingSpaceResponse(
        String uuid,
        String label,
        Integer lotQty,
        Integer filled,
        Integer empty,
        LocalDateTime createdAt,
        String image,
        List<ParkingLotResponse> parkingLots,
        SiteResponse site
) {
}
