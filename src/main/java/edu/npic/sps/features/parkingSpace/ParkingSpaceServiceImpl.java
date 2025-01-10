package edu.npic.sps.features.parkingSpace;

import edu.npic.sps.domain.ParkingSpace;
import edu.npic.sps.features.parkingSpace.dto.LabelResponse;
import edu.npic.sps.features.parkingLot.ParkingLotRepository;
import edu.npic.sps.features.parkingSpace.dto.ParkingSpaceResponse;
import edu.npic.sps.mapper.ParkingSpaceMapper;
import edu.npic.sps.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParkingSpaceServiceImpl implements ParkingSpaceService {

    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ParkingSpaceMapper parkingSpaceMapper;
    private final ParkingLotRepository parkingLotRepository;
    private final AuthUtil authUtil;

    @Override
    public ParkingSpaceResponse findByUuid(String uuid) {

        ParkingSpace parkingSpace = parkingSpaceRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Parking not found!")
        );

        return parkingSpaceMapper.toParkingSpaceResponse(parkingSpace);
    }

//    @Override
//    public void createNew(CreateParking createParking) {
//
//        if (parkingSpaceRepository.existsByLabelContainsIgnoreCase(createParking.label())){
//            throw new ResponseStatusException(
//                    HttpStatus.CONFLICT,
//                    "Parking name already exist!"
//            );
//        }
//
//        Integer slotQty = createParking.parkingSlotsName().size();
//        log.info("slot qty: {}", slotQty);
//
//        ParkingSpace parkingSpace = new ParkingSpace();
//
//        parkingSpace.setSlotQty(slotQty);
//        parkingSpace.setFilled(0);
//        parkingSpace.setEmpty(slotQty);
//        parkingSpace.setLabel(createParking.label());
//        parkingSpace.setUuid(UUID.randomUUID().toString());
//        parkingSpace.setIsDeleted(false);
//        parkingSpace.setCreatedAt(LocalDateTime.now());
//        parkingSpace.setUpdatedAt(LocalDateTime.now());
//
//        parkingSpaceRepository.save(parkingSpace);
//
//        createParking.parkingSlotsName().forEach(
//                slot ->{
//                    ParkingLot newSlot = new ParkingLot();
//                    newSlot.setUuid(UUID.randomUUID().toString());
//                    newSlot.setLotName(slot);
//                    newSlot.setParkingSpace(parkingSpace);
//                    newSlot.setIsAvailable(false);
//                    newSlot.setIsDeleted(false);
//                    parkingLotRepository.save(newSlot);
//                }
//        );
//    }
//
//    @Override
//    public void delete(String uuid) {
//        ParkingSpace parkingSpace = parkingSpaceRepository.findByUuid(uuid).orElseThrow(
//                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking not found")
//        );
//        parkingSpace.setIsDeleted(true);
//        parkingSpaceRepository.save(parkingSpace);
//    }
//
    @Override
    public Page<ParkingSpaceResponse> getAll(int pageNo, int pageSize) {
        boolean isManager = authUtil.isManagerLoggedUser();
        boolean isAdmin = authUtil.isAdminLoggedUser();
        boolean isUser = authUtil.isUserLoggedUser();
        List<String> sites = authUtil.loggedUserSites();

        if (pageNo < 1 || pageSize < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page number or page size must be greater than zero"
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<ParkingSpace> parkingSpaces = Page.empty();

        if (isManager) {
           parkingSpaces = parkingSpaceRepository.findAll(pageRequest);
        }
        else if (isAdmin){
            parkingSpaces = parkingSpaceRepository.findParkingSpacesBySiteUuid(sites.stream().findFirst().orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Site not found!"
            )), pageRequest);
        }

        return parkingSpaces.map(parkingSpaceMapper::toParkingSpaceResponse);
    }

    @Override
    public List<LabelResponse> getAllLabels() {
        boolean isManager = authUtil.isManagerLoggedUser();
        boolean isAdmin = authUtil.isAdminLoggedUser();
        boolean isUser = authUtil.isUserLoggedUser();
        List<String> sites = authUtil.loggedUserSites();

        List<ParkingSpace> parkingSpaceList = new ArrayList<>();

        if (isManager) {
            parkingSpaceList = parkingSpaceRepository.findAll();
        }
        else if (isAdmin || isUser){
            parkingSpaceList = parkingSpaceRepository.findBySite_Uuid(sites.stream().findFirst().orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Site not found!"
            )));
        }

        return parkingSpaceMapper.toLabelResponse(parkingSpaceList);
    }


}
