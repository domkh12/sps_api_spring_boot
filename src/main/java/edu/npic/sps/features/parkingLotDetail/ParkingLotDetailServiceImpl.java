package edu.npic.sps.features.parkingLotDetail;

import edu.npic.sps.domain.ParkingLotDetail;
import edu.npic.sps.domain.ParkingSpace;
import edu.npic.sps.domain.ParkingLot;
import edu.npic.sps.domain.Vehicle;
import edu.npic.sps.features.parkingLotDetail.dto.ParkingDetailResponse;
import edu.npic.sps.features.parkingSpace.ParkingSpaceRepository;
import edu.npic.sps.features.vehicle.VehicleRepository;
import edu.npic.sps.mapper.ParkingLotDetailMapper;
import edu.npic.sps.mapper.ParkingLotMapper;
import edu.npic.sps.mapper.VehicleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParkingLotDetailServiceImpl implements ParkingLotDetailService {

    private final ParkingLotDetailRepository parkingLotDetailRepository;
    private final ParkingLotDetailMapper parkingLotDetailMapper;


    @Override
    public Page<ParkingDetailResponse> findAll(int pageNo, int pageSize) {
        if (pageNo < 1 || pageSize < 1){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page number or page size must greater than 0!");
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<ParkingLotDetail> parkingLotDetails = parkingLotDetailRepository.findAll(pageRequest);
        return parkingLotDetails.map(parkingLotDetailMapper::toParkingDetailResponse);
    }

    @Override
    public ParkingDetailResponse getParkingDetailByUuid(String uuid) {

        ParkingLotDetail parkingLotDetail = parkingLotDetailRepository.findByParkingLot_UuidAndIsParking(uuid, true).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking details not found!")
        );

        return parkingLotDetailMapper.toParkingDetailResponse(parkingLotDetail);
    }
}
