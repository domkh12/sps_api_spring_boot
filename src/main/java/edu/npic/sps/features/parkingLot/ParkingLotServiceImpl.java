package edu.npic.sps.features.parkingLot;

import edu.npic.sps.domain.ParkingLot;
import edu.npic.sps.domain.ParkingLotDetail;
import edu.npic.sps.features.parkingLot.dto.ParkingLotResponse;
import edu.npic.sps.features.parkingLotDetail.ParkingLotDetailRepository;
import edu.npic.sps.features.parkingLot.dto.ParkingSlotDetailResponse;
import edu.npic.sps.mapper.ParkingLotMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ParkingLotServiceImpl implements ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;
    private final ParkingLotMapper parkingLotMapper;
    private final ParkingLotDetailRepository parkingLotDetailRepository;
    private final SimpMessagingTemplate simpMessageTemplate;

    @Override
    public Page<ParkingSlotDetailResponse> findAll(int pageNo, int pageSize) {

        if (pageNo < 1 || pageSize < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page number or page size must be greater than zero"
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<ParkingLotDetail> parkingSlotPage = parkingLotDetailRepository.findAll(pageRequest);
        return parkingSlotPage.map(parkingLotMapper::toParkingSlotDetailResponse);

    }

//    @Override
//    public void createCarParking(CreateCarParking createCarParking) {
//
//        Vehicle vehicle = vehicleRepository.findByNumberPlate(createCarParking.plateNumber()).orElseThrow(
//                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found")
//        );
//
//        ParkingSlot parkingSlot = parkingSlotRepository.findByAlias(createCarParking.alias_slot()).orElseThrow(
//                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking slot not found")
//        );
//
//        if (!userRepository.existsByUuid(createCarParking.userUuid())){
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
//        }
//
//        parkingSlot.setIsAvailable(createCarParking.isAvailable());
//        parkingSlotRepository.save(parkingSlot);
//
//        ParkingSlotDetail parkingSlotDetail = parkingSlotMapper.fromCreateCarParking(createCarParking);
//        parkingSlotDetail.setVehicle(vehicle);
//        parkingSlotDetail.setParkingSlot(parkingSlot);
//        parkingSlotDetail.setIsDeleted(false);
//        parkingSlotDetail.setCreatedAt(LocalDateTime.now());
//
//        parkingSlotDetailRepository.save(parkingSlotDetail);
//
//        updateIsAvailable(
//                createCarParking.userUuid(),
//                ParkingSlotResponse.builder()
//                        .uuid(parkingSlot.getUuid())
//                        .slotName(parkingSlot.getSlotName())
//                        .isAvailable(parkingSlot.getIsAvailable())
//                        .build()
//        );
//
//    }

    @Override
    public ParkingSlotDetailResponse findByUuid(String uuid) {

        ParkingLot parkingLot = parkingLotRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "ParkingSlot not found!"
                )
        );

        ParkingLotDetail parkingLotDetail = parkingLotDetailRepository.findById(parkingLot.getId()).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "ParkingSlot Detail not found!"
                )
        );

        return parkingLotMapper.toParkingSlotDetailResponse(parkingLotDetail);
    }

    public void updateIsAvailable(String userId, ParkingLotResponse parkingLotResponse) {
//        log.info("Sending WS update to user : {}", userId, parkingSlotResponse);
        simpMessageTemplate.convertAndSendToUser(
                userId,
                "/update",
                parkingLotResponse
        );
    }
}
