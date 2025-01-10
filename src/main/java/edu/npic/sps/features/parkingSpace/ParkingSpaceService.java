package edu.npic.sps.features.parkingSpace;

import edu.npic.sps.features.parkingSpace.dto.LabelResponse;
import edu.npic.sps.features.parkingSpace.dto.ParkingSpaceResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ParkingSpaceService {

    ParkingSpaceResponse findByUuid(String uuid);
//
//    void createNew(CreateParking createParking);
//
//    void delete(String uuid);
//
    Page<ParkingSpaceResponse> getAll(int pageNo, int pageSize);

    List<LabelResponse> getAllLabels();
}
