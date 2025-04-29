package edu.npic.sps.features.parkingSpace;

import edu.npic.sps.features.parkingSpace.dto.CreateParkingSpace;
import edu.npic.sps.features.parkingSpace.dto.LabelResponse;
import edu.npic.sps.features.parkingSpace.dto.ParkingSpaceRequest;
import edu.npic.sps.features.parkingSpace.dto.ParkingSpaceResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ParkingSpaceService {

    ParkingSpaceResponse findByUuid(String uuid);

    Page<ParkingSpaceResponse> findAll(int pageNo, int pageSize);

    List<LabelResponse> getAllLabels();

    ParkingSpaceResponse create(CreateParkingSpace createParkingSpace);

    void delete(String uuid);

    ParkingSpaceResponse update(String uuid, ParkingSpaceRequest parkingSpaceRequest);

    Page<ParkingSpaceResponse> filter(int pageNo, int pageSize, String branchUuid, String keywords);
}
