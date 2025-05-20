package edu.npic.sps.features.vehicle;

import edu.npic.sps.features.parkingLotDetail.dto.ParkingDetailResponse;
import edu.npic.sps.features.vehicle.dto.CreateVehicle;
import edu.npic.sps.features.vehicle.dto.VehicleRequest;
import edu.npic.sps.features.vehicle.dto.VehicleResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vehicles")
@Tag(name = "Vehicle Management", description = "APIs for managing vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/check-out")
    @ResponseStatus(HttpStatus.CREATED)
    ParkingDetailResponse checkOut(
            @RequestParam String numberPlate,
            @RequestParam(required = false) String vehicleModel,
            @RequestParam(required = false) String vehicleMake,
            @RequestParam(required = false) String color
    ){
        return vehicleService.checkOut(numberPlate, vehicleModel, vehicleMake, color);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping("/check-in")
    @ResponseStatus(HttpStatus.CREATED)
    ParkingDetailResponse checkIn(
            @RequestParam String numberPlate,
            @RequestParam String provincePlate,
            @RequestParam(required = false) String vehicleModel,
            @RequestParam(required = false) String vehicleMake,
            @RequestParam(required = false) String color,
            @RequestParam String space,
            @RequestParam String lot
    ) {
        return vehicleService.checkIn(numberPlate, provincePlate, vehicleModel, vehicleMake, color, space, lot);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/filters")
    @ResponseStatus(HttpStatus.OK)
    Page<VehicleResponse> filter(@RequestParam(required = false, defaultValue = "1") int pageNo,
                                 @RequestParam(required = false, defaultValue = "20") int pageSize,
                                 @RequestParam(required = false, defaultValue = "") String keywords,
                                 @RequestParam(required = false, defaultValue = "") String vehicleTypeId,
                                 @RequestParam(required = false, defaultValue = "") String branchId
                                 ){
        return vehicleService.filter(pageNo, pageSize, keywords, vehicleTypeId, branchId);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable String uuid) {
        vehicleService.delete(uuid);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PatchMapping("/{uuid}")
    @ResponseStatus(HttpStatus.CREATED)
    VehicleResponse update(@PathVariable String uuid, @Valid @RequestBody VehicleRequest vehicleRequest){
        return vehicleService.update(uuid, vehicleRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    VehicleResponse create(@Valid @RequestBody CreateVehicle createVehicle){
        return vehicleService.create(createVehicle);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    Page<VehicleResponse> findAll(@RequestParam(required = false,defaultValue = "1") int pageNo,
                                     @RequestParam(required = false,defaultValue = "20") int pageSize){
        return vehicleService.findAll(pageNo, pageSize);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{numberPlate}")
    VehicleResponse findByNumPlate(@PathVariable String numberPlate){
        return vehicleService.findByNumPlate(numberPlate);
    }

}
