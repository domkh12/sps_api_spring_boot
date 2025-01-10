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
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParkingLotDetailServiceImpl implements ParkingLotDetailService {

    private final VehicleRepository vehicleRepository;
    private final ParkingSpaceRepository parkingSpaceRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ParkingLotDetailRepository parkingLotDetailRepository;
    private final VehicleMapper vehicleMapper;
    private final ParkingLotMapper parkingLotMapper;
    private final ParkingLotDetailMapper parkingLotDetailMapper;

    @Override
    public void createCarParking(String numberPlate, String locationId, String slotId) {

        if (!vehicleRepository.existsByNumberPlate(numberPlate)){
            Vehicle vehicle = new Vehicle();
            vehicle.setUuid(UUID.randomUUID().toString());
            vehicle.setNumberPlate(numberPlate);
            vehicle.setIsDeleted(false);
            vehicle.setCreatedAt(LocalDateTime.now());
            vehicleRepository.save(vehicle);
        }

        Vehicle vehicle = vehicleRepository.findByNumberPlate(numberPlate).orElseThrow();

        ParkingSpace p = parkingSpaceRepository.findByUuid(locationId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking not found!")
        );
        p.setFilled(p.getFilled()+1);
        p.setEmpty(p.getEmpty()-1);


        ParkingLot parkingLot = p.getParkingLots().stream().filter(
                s -> s.getUuid().equals(slotId)
        ).findFirst().orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking slot not found in the location = "+ p.getLabel())
        );

        parkingLot.setIsAvailable(false);
        ParkingLotDetail parkingLotDetail = new ParkingLotDetail();
        parkingLotDetail.setUuid(UUID.randomUUID().toString());
        parkingLotDetail.setIsParking(true);
        parkingLotDetail.setTimeIn(LocalDateTime.now());
        parkingLotDetail.setVehicle(vehicle);
        parkingLotDetail.setParkingLot(parkingLot);
        parkingLotDetail.setParkingSpace(p);
        parkingLotDetail.setIsDeleted(false);
        parkingLotDetail.setCreatedAt(LocalDateTime.now());
        parkingSpaceRepository.save(p);
        parkingLotDetailRepository.save(parkingLotDetail);

        simpMessagingTemplate.convertAndSend(
                "/topic/slot-update",
                parkingLotDetailMapper.toParkingDetailResponse(parkingLotDetail)
        );
    }

    @Override
    public ParkingDetailResponse getParkingDetailByUuid(String uuid) {

        ParkingLotDetail parkingLotDetail = parkingLotDetailRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking not found")
        );

//        return ParkingDetailResponse.builder()
//                .uuid(parkingSlotDetail.getUuid())
//                .timeIn(parkingSlotDetail.getTimeIn())
//                .timeOut(parkingSlotDetail.getTimeOut())
//                .isParking(parkingSlotDetail.getIsParking())
//                .vehicleResponse(vehicleMapper.toVehicleResponse(parkingSlotDetail.getVehicle()))
//                .parkingSlotResponse(parkingSlotMapper.toParkingSlotResponse(parkingSlotDetail.getParkingSlot()))
//                .createdAt(parkingSlotDetail.getCreatedAt())
//                .build();
        return null;
    }
}
