package edu.npic.sps.features.parkingLotDetail;

import edu.npic.sps.features.parkingLotDetail.dto.ParkingDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/parking-details")
@RequiredArgsConstructor
public class ParkingLotDetailController {

    private final ParkingLotDetailService parkingLotDetailService;

//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void createCarParking(
            @RequestParam String numberPlate,
            @RequestParam String locationId,
            @RequestParam String slotId
    ){
        parkingLotDetailService.createCarParking(numberPlate, locationId, slotId);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    ParkingDetailResponse getParkingDetailByUuid(@PathVariable String uuid){
        return parkingLotDetailService.getParkingDetailByUuid(uuid);
    }

}
