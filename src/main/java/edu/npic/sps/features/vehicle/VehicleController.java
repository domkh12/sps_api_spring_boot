package edu.npic.sps.features.vehicle;

import edu.npic.sps.features.vehicle.dto.CreateVehicle;
import edu.npic.sps.features.vehicle.dto.VehicleRequest;
import edu.npic.sps.features.vehicle.dto.VehicleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @PatchMapping("/{uuid}")
    VehicleResponse update(@PathVariable String uuid, @Valid @RequestBody VehicleRequest vehicleRequest){
        return vehicleService.update(uuid, vehicleRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    VehicleResponse create(@Valid @RequestBody CreateVehicle createVehicle){
        return vehicleService.create(createVehicle);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping
    Page<VehicleResponse> findAll(@RequestParam(required = false,defaultValue = "1") int pageNo,
                                     @RequestParam(required = false,defaultValue = "20") int pageSize){
        return vehicleService.findAll(pageNo, pageSize);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping("{numberPlate}")
    VehicleResponse findByNumPlate(@PathVariable String numberPlate){
        return vehicleService.findByNumPlate(numberPlate);
    }

}
