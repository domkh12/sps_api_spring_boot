package edu.npic.sps.features.parkingLot;

import edu.npic.sps.domain.ParkingLot;
import edu.npic.sps.domain.ParkingLotDetail;
import edu.npic.sps.domain.ParkingSpace;
import edu.npic.sps.features.parkingLot.dto.*;
import edu.npic.sps.features.parkingLotDetail.ParkingLotDetailRepository;
import edu.npic.sps.features.parkingSpace.ParkingSpaceRepository;
import edu.npic.sps.mapper.ParkingLotMapper;
import edu.npic.sps.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParkingLotServiceImpl implements ParkingLotService {
    private final ParkingLotRepository parkingLotRepository;
    private final ParkingLotMapper parkingLotMapper;
    private final ParkingLotDetailRepository parkingLotDetailRepository;
    private final SimpMessagingTemplate simpMessageTemplate;
    private final ParkingSpaceRepository parkingSpaceRepository;
    private final AuthUtil authUtil;

    @Override
    public void deleteParkingSlot(String uuid) {
        ParkingLot parkingLot = parkingLotRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking lot not found!")
        );

        parkingLotRepository.deleteByUuid(parkingLot.getUuid());
        ParkingSpace parkingSpace = parkingLot.getParkingSpace();
        parkingSpace.setLotQty(parkingSpace.getLotQty() - 1);
        parkingSpace.setEmpty(parkingSpace.getEmpty() - 1);
        parkingSpaceRepository.save(parkingSpace);
    }

    @Override
    public Page<ParkingLotResponse>


    filter(int pageNo, int pageSize, List<String> branchUuid, String keywords) {
        boolean isAdmin = authUtil.isAdminLoggedUser();
        boolean isManager = authUtil.isManagerLoggedUser();
        List<String> siteUuid = authUtil.loggedUserSites();

        if (pageNo < 1 || pageSize < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page number or page size must be greater than zero"
            );
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<ParkingLot> parkingLotPage = Page.empty();
        if (isAdmin) {
            parkingLotPage = parkingLotRepository.filterParkingLot(
                    keywords,
                    branchUuid.isEmpty() ? null : branchUuid,
                    pageRequest
            );
        }else if (isManager) {
            parkingLotPage = parkingLotRepository.filterParkingLot(
                    keywords,
                    siteUuid.isEmpty() ? null : siteUuid,
                    pageRequest
            );
        }

        return parkingLotPage.map(parkingLotMapper::toParkingSlotResponse);
    }

    @Override
    public List<ParkingLotResponse> createMultipleParkingLot(CreateMultipleSlot createMultipleSlot) {
        List<ParkingLot> createdParkingLots = new ArrayList<>();

        createMultipleSlot.slots().forEach(parkingLotReq -> {
            ParkingSpace parkingSpace = parkingSpaceRepository.findByUuid(parkingLotReq.parkingSpaceUuid()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking space not found!")
            );

            ParkingLot parkingLot = parkingLotMapper.fromParkingLotRequest(parkingLotReq);
            parkingLot.setUuid(UUID.randomUUID().toString());
            parkingLot.setLotName(parkingLotReq.lotName());
            parkingLot.setIsAvailable(true);
            parkingLot.setCreatedAt(LocalDateTime.now());
            parkingLot.setParkingSpace(parkingSpace);
            parkingSpace.setLotQty(parkingSpace.getLotQty() + 1);
            parkingSpace.setEmpty(parkingSpace.getEmpty() + 1);
            parkingLotRepository.save(parkingLot);
            parkingSpaceRepository.save(parkingSpace);

            createdParkingLots.add(parkingLot);
        });

        return createdParkingLots.stream().map(parkingLotMapper::toParkingSlotResponse).toList();
    }

    @Override
    public ParkingLotResponse findParkingLotByUuid(String uuid) {
        boolean isAdmin = authUtil.isAdminLoggedUser();
        boolean isManager = authUtil.isManagerLoggedUser();
        List<String> siteUuid = authUtil.loggedUserSites();

        if (!parkingLotRepository.existsByParkingSpace_Site_UuidInAndUuid(siteUuid, uuid) && isManager) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking slot not found!");
        }

        ParkingLot parkingLot = parkingLotRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking slot not found!")
        );
        return parkingLotMapper.toParkingSlotResponse(parkingLot);
    }

    @Override
    public ParkingLotResponse update(String uuid, ParkingLotRequest parkingLotRequest) {

        ParkingLot parkingLot = parkingLotRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking lot not found!")
        );

        ParkingSpace parkingSpace = parkingSpaceRepository.findByUuid(parkingLotRequest.parkingSpaceUuid()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking space not found!")
        );

        parkingLotMapper.updateFromParkingLotRequest(parkingLotRequest, parkingLot);
        parkingLot.setParkingSpace(parkingSpace);
        ParkingLot savedParkingLot = parkingLotRepository.save(parkingLot);

        return parkingLotMapper.toParkingSlotResponse(savedParkingLot);
    }

    @Override
    public ParkingLotResponse create(ParkingLotRequest parkingLotRequest) {

        ParkingSpace parkingSpace = parkingSpaceRepository.findByUuid(parkingLotRequest.parkingSpaceUuid()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking space not found!")
        );

        if (parkingLotRepository.existsByLotNameIgnoreCaseAndParkingSpace_Uuid(parkingLotRequest.lotName(), parkingLotRequest.parkingSpaceUuid())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Parking lot with this name already exists in the selected parking space"
            );
        }

        ParkingLot parkingLot = parkingLotMapper.fromParkingLotRequest(parkingLotRequest);
        parkingLot.setUuid(UUID.randomUUID().toString());
        parkingLot.setCreatedAt(LocalDateTime.now());
        parkingLot.setIsAvailable(true);
        parkingLot.setParkingSpace(parkingSpace);
        ParkingLot parkingLotSaved = parkingLotRepository.save(parkingLot);
        parkingSpace.setLotQty(parkingSpace.getLotQty() + 1);
        parkingSpace.setEmpty(parkingSpace.getEmpty() + 1);
        parkingSpaceRepository.save(parkingSpace);
        return parkingLotMapper.toParkingSlotResponse(parkingLotSaved);
    }

    @Override
    public Page<ParkingLotResponse> findAll(int pageNo, int pageSize) {
        boolean isAdmin = authUtil.isAdminLoggedUser();
        boolean isManager = authUtil.isManagerLoggedUser();
        List<String> branchUuid = authUtil.loggedUserSites();

        if (pageNo < 1 || pageSize < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page number or page size must be greater than zero"
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<ParkingLot> parkingSlotPage = Page.empty();
        if (isAdmin) {
            parkingSlotPage = parkingLotRepository.findAll(pageRequest);
        }else if (isManager) {
            parkingSlotPage = parkingLotRepository.findByParkingSpace_Site_UuidIn(branchUuid, pageRequest);
        }
        return parkingSlotPage.map(parkingLotMapper::toParkingSlotResponse);

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