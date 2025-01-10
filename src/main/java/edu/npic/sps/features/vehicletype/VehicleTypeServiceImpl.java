package edu.npic.sps.features.vehicletype;

import edu.npic.sps.domain.VehicleType;
import edu.npic.sps.features.vehicle.dto.CreateVehicleType;
import edu.npic.sps.features.vehicletype.dto.UpdateRequest;
import edu.npic.sps.features.vehicletype.dto.VehicleTypeRequest;
import edu.npic.sps.features.vehicletype.dto.VehicleTypeResponse;
import edu.npic.sps.mapper.VehicleTypeMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleTypeServiceImpl implements VehicleTypeService{

    private final VehicleTypeRepository vehicleTypeRepository;
    private final VehicleTypeMapper vehicleTypeMapper;

    @Override
    public VehicleTypeResponse update(String uuid, UpdateRequest updateRequest) {

        VehicleType vehicleType = vehicleTypeRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle type not found")
        );

        vehicleTypeMapper.fromUpdateRequest(updateRequest, vehicleType);
//        vehicleType.setName(vehicleType.getName());
        vehicleTypeRepository.save(vehicleType);

        return vehicleTypeMapper.toVehicleTypeResponse(vehicleType);
    }

    @Transactional
    @Override
    public void deleteByUuid(String uuid) {

        VehicleType vehicleType = vehicleTypeRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle Type Not Found")
        );

        vehicleTypeRepository.unsetVehicleType(vehicleType);
        vehicleTypeRepository.delete(vehicleType);
    }

    @Override
    public VehicleTypeResponse createNew(CreateVehicleType createVehicleType) {

        if (vehicleTypeRepository.existsByAlias(createVehicleType.alias())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle type already exists!");
        }

        VehicleType vehicleType = new VehicleType();
        vehicleType.setUuid(UUID.randomUUID().toString());
        vehicleType.setAlias(createVehicleType.name());
        vehicleType.setName(createVehicleType.name());
        vehicleType.setVehicle(new ArrayList<>());
        vehicleTypeRepository.save(vehicleType);
        return vehicleTypeMapper.toVehicleTypeResponse(vehicleType);
    }

    @Override
    public List<VehicleTypeResponse> findAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<VehicleType> vehicleTypeList = vehicleTypeRepository.findAll(sort);
        return vehicleTypeMapper.toListVehicleTypeResponse(vehicleTypeList);
    }
}
