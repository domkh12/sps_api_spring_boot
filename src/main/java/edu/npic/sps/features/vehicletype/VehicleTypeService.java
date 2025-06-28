package edu.npic.sps.features.vehicletype;

import edu.npic.sps.features.vehicle.dto.CreateVehicleType;
import edu.npic.sps.features.vehicle.dto.VehicleResponse;
import edu.npic.sps.features.vehicletype.dto.UpdateRequest;
import edu.npic.sps.features.vehicletype.dto.VehicleTypeRequest;
import edu.npic.sps.features.vehicletype.dto.VehicleTypeResponse;

import java.util.List;

public interface VehicleTypeService {

    VehicleTypeResponse update(String uuid, UpdateRequest updateRequest);

    void deleteByUuid(String uuid);

    VehicleTypeResponse createNew(CreateVehicleType createVehicleType);

    List<VehicleTypeResponse> findAll();
}
