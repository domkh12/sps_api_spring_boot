package edu.npic.sps.features.parkingLot;

import edu.npic.sps.features.parkingLot.dto.CreateParkingLot;
import edu.npic.sps.features.parkingLot.dto.ParkingLotRequest;
import edu.npic.sps.features.parkingLot.dto.ParkingLotResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parking-lots")
@RequiredArgsConstructor
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

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
