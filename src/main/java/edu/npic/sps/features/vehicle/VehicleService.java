package edu.npic.sps.features.vehicle;

import edu.npic.sps.features.vehicle.dto.CreateVehicle;
import edu.npic.sps.features.vehicle.dto.VehicleRequest;
import edu.npic.sps.features.vehicle.dto.VehicleResponse;
import org.springframework.data.domain.Page;

public interface VehicleService {

    VehicleResponse update(String uuid, VehicleRequest vehicleRequest);

    VehicleResponse create(CreateVehicle createVehicle);

    VehicleResponse findByNumPlate(String numberPlate);

    Page<VehicleResponse> findAll(int pageNo, int pageSize);
}
