package edu.npic.sps.features.parkingLot;

import edu.npic.sps.features.parkingLot.dto.CreateParkingLot;
import edu.npic.sps.features.parkingLot.dto.ParkingLotRequest;
import edu.npic.sps.features.parkingLot.dto.ParkingLotResponse;
import edu.npic.sps.features.parkingLot.dto.ParkingSlotDetailResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parking-lots")
@RequiredArgsConstructor
@Tag(name = "Parking lot Management", description = "APIs for managing Parking lot")
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<ParkingLotResponse> findAll(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "20") int pageSize
    ){
        return parkingLotService.findAll(pageNo, pageSize);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    ParkingLotResponse findByUuid(@PathVariable String uuid) {
        return parkingLotService.findParkingLotByUuid(uuid);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PatchMapping("/{uuid}")
    @ResponseStatus(HttpStatus.CREATED)
    List<ParkingLotResponse> update(@PathVariable String uuid, @RequestBody ParkingLotRequest parkingLotRequest){
        return parkingLotService.update(uuid, parkingLotRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    List<ParkingLotResponse> create(@RequestBody CreateParkingLot createParkingLot){
        return parkingLotService.create(createParkingLot);
    }

}
