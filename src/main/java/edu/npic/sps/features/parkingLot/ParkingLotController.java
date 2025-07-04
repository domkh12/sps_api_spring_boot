package edu.npic.sps.features.parkingLot;

import edu.npic.sps.features.parkingLot.dto.*;
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

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteParkingSlot(@PathVariable String uuid) {
        parkingLotService.deleteParkingSlot(uuid);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    Page<ParkingLotResponse> filter(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "20") int pageSize,
            @RequestParam(required = false) List<String> branchUuid,
            @RequestParam(required = false) String keywords
    ) {
        return parkingLotService.filter(pageNo, pageSize, branchUuid, keywords);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<ParkingLotResponse> findAll(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "20") int pageSize
    ){
        return parkingLotService.findAll(pageNo, pageSize);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    ParkingLotResponse findByUuid(@PathVariable String uuid) {
        return parkingLotService.findParkingLotByUuid(uuid);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.CREATED)
    ParkingLotResponse update(@PathVariable String uuid, @RequestBody ParkingLotRequest parkingLotRequest){
        return parkingLotService.update(uuid, parkingLotRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ParkingLotResponse create(@RequestBody ParkingLotRequest parkingLotRequest){
        return parkingLotService.create(parkingLotRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/multiple")
    @ResponseStatus(HttpStatus.CREATED)
    List<ParkingLotResponse> createMultiple(@RequestBody CreateMultipleSlot createMultipleSlot) {
        return parkingLotService.createMultipleParkingLot(createMultipleSlot);
    }

}
