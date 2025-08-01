package edu.npic.sps.features.vehicle;

import edu.npic.sps.features.parkingLotDetail.dto.ParkingDetailResponse;
import edu.npic.sps.features.vehicle.dto.CameraRequest;
import edu.npic.sps.features.vehicle.dto.CreateVehicle;
import edu.npic.sps.features.vehicle.dto.VehicleRequest;
import edu.npic.sps.features.vehicle.dto.VehicleResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface VehicleService {

    ParkingDetailResponse checkOut(@Valid CameraRequest cameraRequest);

    Page<ParkingDetailResponse> filterCheckOut(int pageNo, int pageSize, String keywords, LocalDateTime dateFrom, LocalDateTime dateTo);

    Page<ParkingDetailResponse> getAllCheckOut(int pageNo, int pageSize);

    Page<ParkingDetailResponse> filterCheckIn(int pageNo, int pageSize, String keywords, LocalDateTime dateFrom, LocalDateTime dateTo);

    Page<ParkingDetailResponse> getAllCheckIn(int pageNo, int pageSize);

    ParkingDetailResponse checkIn(@Valid CameraRequest cameraRequest);

    Page<VehicleResponse> getVehicleReport(int pageNo, int pageSize, LocalDateTime dateFrom, LocalDateTime dateTo);

    VehicleResponse getVehicleByUuid(String uuid);

    VehicleResponse update(String uuid, VehicleRequest vehicleRequest);

    VehicleResponse create(CreateVehicle createVehicle);

    VehicleResponse findByNumPlate(String numberPlate);

    Page<VehicleResponse> findAll(int pageNo, int pageSize);

    void delete(String uuid);

    Page<VehicleResponse> filter(int pageNo, int pageSize, String keywords, String vehicleTypeId, String branchId);
}
