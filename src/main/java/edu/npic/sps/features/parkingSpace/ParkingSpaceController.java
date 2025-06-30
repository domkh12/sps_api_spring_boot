package edu.npic.sps.features.parkingSpace;

import edu.npic.sps.features.parkingSpace.dto.CreateParkingSpace;
import edu.npic.sps.features.parkingSpace.dto.ParkingNameResponse;
import edu.npic.sps.features.parkingSpace.dto.ParkingSpaceRequest;
import edu.npic.sps.features.parkingSpace.dto.ParkingSpaceResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parking-spaces")
@RequiredArgsConstructor
@Tag(name = "Parking space Management", description = "APIs for managing parking spaces")
public class ParkingSpaceController {

    private final ParkingSpaceService parkingSpaceService;

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @GetMapping("/filters")
    @ResponseStatus(HttpStatus.OK)
    Page<ParkingSpaceResponse> filter(@RequestParam(required = false, defaultValue = "1") int pageNo,
                                      @RequestParam(required = false, defaultValue = "20") int pageSize,
                                      @RequestParam(required = false, defaultValue = "") String branchUuid,
                                      @RequestParam(required = false, defaultValue = "") String keywords
    ){
        return parkingSpaceService.filter(pageNo, pageSize, branchUuid, keywords);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PatchMapping("/{uuid}")
    @ResponseStatus(HttpStatus.CREATED)
    ParkingSpaceResponse update(@PathVariable String uuid, @RequestBody ParkingSpaceRequest parkingSpaceRequest){
        return parkingSpaceService.update(uuid, parkingSpaceRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/list")
    List<ParkingNameResponse> getAllLabels() {
        return parkingSpaceService.getAllLabels();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/{uuid}")
    ParkingSpaceResponse findByUuid(@PathVariable String uuid) {
        return parkingSpaceService.findByUuid(uuid);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ParkingSpaceResponse create(@Valid @RequestBody CreateParkingSpace createParkingSpace) {
       return parkingSpaceService.create(createParkingSpace);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable String uuid) {
         parkingSpaceService.delete(uuid);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN', 'ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    Page<ParkingSpaceResponse> findAll(@RequestParam(required = false, defaultValue = "1") int pageNo,
                                      @RequestParam(required = false, defaultValue = "20") int pageSize){
        return parkingSpaceService.findAll(pageNo,pageSize);
    }
}
