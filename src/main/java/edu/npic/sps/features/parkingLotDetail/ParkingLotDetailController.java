package edu.npic.sps.features.parkingLotDetail;

import edu.npic.sps.features.parkingLotDetail.dto.ParkingDetailResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/parking-slot-details")
@RequiredArgsConstructor
@Tag(name = "Parking lot details Management", description = "APIs for managing parking lot details")
public class ParkingLotDetailController {

    private final ParkingLotDetailService parkingLotDetailService;

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/filters")
    @ResponseStatus(HttpStatus.OK)
    Page<ParkingDetailResponse> filter(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "20") int pageSize,
            @RequestParam(required = false, defaultValue = "") String keywords,
            @RequestParam(required = false, defaultValue = "") LocalDateTime dateFrom,
            @RequestParam(required = false, defaultValue = "") LocalDateTime dateTo
    ){
        return parkingLotDetailService.filter(pageNo, pageSize, keywords, dateFrom, dateTo);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<ParkingDetailResponse> findAll(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "20") int pageSize
            ){
        return parkingLotDetailService.findAll(pageNo, pageSize);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER')")
    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    ParkingDetailResponse getParkingDetailByUuid(@PathVariable String uuid){
        return parkingLotDetailService.getParkingDetailByUuid(uuid);
    }

}
