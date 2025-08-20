package edu.npic.sps.features.vehicle;

import edu.npic.sps.features.parkingLotDetail.dto.ParkingDetailResponse;
import edu.npic.sps.features.vehicle.dto.CameraRequest;
import edu.npic.sps.features.vehicle.dto.CreateVehicle;
import edu.npic.sps.features.vehicle.dto.VehicleRequest;
import edu.npic.sps.features.vehicle.dto.VehicleResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.fonts.SimpleFontExtensionsRegistryFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vehicles")
@Tag(name = "Vehicle Management", description = "APIs for managing vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/report/excel")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<InputStreamResource> getVehicleReportExcel() throws IOException {
        return vehicleService.getVehicleReportExcel();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/report/pdf")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<InputStreamResource> getVehicleReportPdf() throws IOException {
        return vehicleService.getVehicleReportPdf();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/report")
    @ResponseStatus(HttpStatus.OK)
    Page<VehicleResponse> getVehicleReport(@RequestParam(required = false, defaultValue = "1") int pageNo,
                                           @RequestParam(required = false, defaultValue = "20") int pageSize,
                                           @RequestParam LocalDateTime dateFrom,
                                           @RequestParam LocalDateTime dateTo
                                           ){
        return vehicleService.getVehicleReport(pageNo, pageSize, dateFrom, dateTo);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/uuid/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    VehicleResponse getVehicleByUuid(@PathVariable String uuid) {
        return vehicleService.getVehicleByUuid(uuid);
    }

    @PostMapping("/check-out")
    @ResponseStatus(HttpStatus.CREATED)
    ParkingDetailResponse checkOut(@Valid @RequestBody CameraRequest cameraRequest){
        return vehicleService.checkOut(cameraRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/check-out/filters")
    @ResponseStatus(HttpStatus.OK)
    Page<ParkingDetailResponse> filterCheckOut(@RequestParam(required = false, defaultValue = "1") int pageNo,
                                              @RequestParam(required = false, defaultValue = "20") int pageSize,
                                              @RequestParam(required = false, defaultValue = "") String keywords,
                                              @RequestParam(required = false, defaultValue = "") LocalDateTime dateFrom,
                                              @RequestParam(required = false, defaultValue = "") LocalDateTime dateTo
    ){
        return vehicleService.filterCheckOut(pageNo, pageSize, keywords, dateFrom, dateTo);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/check-out")
    @ResponseStatus(HttpStatus.OK)
    Page<ParkingDetailResponse> getAllCheckOut(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "20") int pageSize
    ){
        return vehicleService.getAllCheckOut(pageNo, pageSize);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/check-in/filters")
    @ResponseStatus(HttpStatus.OK)
    Page<ParkingDetailResponse> filterCheckIn(@RequestParam(required = false, defaultValue = "1") int pageNo,
                                              @RequestParam(required = false, defaultValue = "20") int pageSize,
                                              @RequestParam(required = false, defaultValue = "") String keywords,
                                              @RequestParam(required = false, defaultValue = "") LocalDateTime dateFrom,
                                              @RequestParam(required = false, defaultValue = "") LocalDateTime dateTo
    ){
        return vehicleService.filterCheckIn(pageNo, pageSize, keywords, dateFrom, dateTo);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/check-in")
    @ResponseStatus(HttpStatus.OK)
    Page<ParkingDetailResponse> getAllCheckIn(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "20") int pageSize
    ){
        return vehicleService.getAllCheckIn(pageNo, pageSize);
    }

    @PostMapping("/check-in")
    @ResponseStatus(HttpStatus.CREATED)
    ParkingDetailResponse checkIn(@Valid @RequestBody CameraRequest cameraRequest) {
        return vehicleService.checkIn(cameraRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
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

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable String uuid) {
        vehicleService.delete(uuid);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PatchMapping("/{uuid}")
    @ResponseStatus(HttpStatus.CREATED)
    VehicleResponse update(@PathVariable String uuid, @Valid @RequestBody VehicleRequest vehicleRequest){
        return vehicleService.update(uuid, vehicleRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    VehicleResponse create(@Valid @RequestBody CreateVehicle createVehicle){
        return vehicleService.create(createVehicle);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
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
