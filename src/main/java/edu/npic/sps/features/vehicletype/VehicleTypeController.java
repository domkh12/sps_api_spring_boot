package edu.npic.sps.features.vehicletype;

import edu.npic.sps.features.vehicle.dto.CreateVehicleType;
import edu.npic.sps.features.vehicle.dto.VehicleResponse;
import edu.npic.sps.features.vehicletype.dto.UpdateRequest;
import edu.npic.sps.features.vehicletype.dto.VehicleTypeRequest;
import edu.npic.sps.features.vehicletype.dto.VehicleTypeResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehicle-types")
@RequiredArgsConstructor
@Tag(name = "Vehicle type Management", description = "APIs for managing vehicle type")
public class VehicleTypeController {

    private final VehicleTypeService vehicleTypeService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{uuid}")
    VehicleTypeResponse update(@PathVariable String uuid, @RequestBody UpdateRequest updateRequest){
        return vehicleTypeService.update(uuid, updateRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{uuid}")
    void deleteByUuid(@PathVariable String uuid) {
        vehicleTypeService.deleteByUuid(uuid);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping
    List<VehicleTypeResponse> findAll() {
        return vehicleTypeService.findAll();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    VehicleTypeResponse createNew(@Valid @RequestBody CreateVehicleType createVehicleType){
        return vehicleTypeService.createNew(createVehicleType);
    }

}
