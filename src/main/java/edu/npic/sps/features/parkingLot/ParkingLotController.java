package edu.npic.sps.features.parkingLot;

import edu.npic.sps.features.parkingLot.dto.ParkingSlotDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/parking-lots")
@RequiredArgsConstructor
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @GetMapping
//    Page<ParkingSlotDetailResponse> findAll(@RequestParam(required = false, defaultValue = "1") int pageNo,
//                                            @RequestParam(required = false, defaultValue = "30") int pageSize) {
//        return parkingLotService.findAll(pageNo,pageSize);
//    }
//
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @ResponseStatus(HttpStatus.CREATED)
//    @GetMapping("/{uuid}")
//    ParkingSlotDetailResponse findByUuid(@PathVariable String uuid) {
//        return parkingLotService.findByUuid(uuid);
//    }

//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @PostMapping("/vehicle-parking")
//    @ResponseStatus(HttpStatus.CREATED)
//    void createCarParking(@RequestBody CreateCarParking createCarParking){
//        parkingSlotService.createCarParking(createCarParking);
//    }

}
