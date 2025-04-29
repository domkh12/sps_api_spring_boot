package edu.npic.sps.features.vehicle;

import edu.npic.sps.features.parkingLotDetail.dto.ParkingDetailResponse;
import edu.npic.sps.features.vehicle.dto.CreateVehicle;
import edu.npic.sps.features.vehicle.dto.VehicleRequest;
import edu.npic.sps.features.vehicle.dto.VehicleResponse;
import org.springframework.data.domain.Page;

public interface VehicleService {

    VehicleResponse update(String uuid, VehicleRequest vehicleRequest);

    VehicleResponse create(CreateVehicle createVehicle);

    VehicleResponse findByNumPlate(String numberPlate);

    Page<VehicleResponse> findAll(int pageNo, int pageSize);

    void delete(String uuid);

    Page<VehicleResponse> filter(int pageNo, int pageSize, String keywords, String vehicleTypeId, String branchId);

    ParkingDetailResponse checkIn(String numberPlate, String provincePlate, String vehicleModel, String vehicleMake, String color, String space, String lot);

    ParkingDetailResponse checkOut(String numberPlate, String vehicleModel, String vehicleMake, String color);
}
