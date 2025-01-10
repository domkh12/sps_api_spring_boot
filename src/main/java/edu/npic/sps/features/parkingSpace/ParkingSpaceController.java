package edu.npic.sps.features.parkingSpace;

import edu.npic.sps.features.parkingSpace.dto.LabelResponse;
import edu.npic.sps.features.parkingSpace.dto.ParkingSpaceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parking-spaces")
@RequiredArgsConstructor
public class ParkingSpaceController {

    private final ParkingSpaceService parkingSpaceService;

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/labels")
    List<LabelResponse> getAllLabels() {
        return parkingSpaceService.getAllLabels();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/{uuid}")
    ParkingSpaceResponse findByUuid(@PathVariable String uuid) {
        return parkingSpaceService.findByUuid(uuid);
    }
//
////    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    void createNew(@Valid @RequestBody CreateParking createParking) {
//        parkingSpaceService.createNew(createParking);
//    }
//
    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN', 'ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping
    Page<ParkingSpaceResponse> getAll(@RequestParam(required = false, defaultValue = "1") int pageNo,
                                      @RequestParam(required = false, defaultValue = "20") int pageSize){
        return parkingSpaceService.getAll(pageNo,pageSize);
    }
//
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @DeleteMapping("/{uuid}")
//    void delete(@PathVariable String uuid){
//        parkingSpaceService.delete(uuid);
//    }
}
