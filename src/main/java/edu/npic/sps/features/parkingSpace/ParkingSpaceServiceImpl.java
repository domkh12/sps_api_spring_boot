package edu.npic.sps.features.parkingSpace;

import edu.npic.sps.domain.ParkingLot;
import edu.npic.sps.domain.ParkingSpace;
import edu.npic.sps.domain.Site;
import edu.npic.sps.features.parkingSpace.dto.CreateParkingSpace;
import edu.npic.sps.features.parkingSpace.dto.ParkingNameResponse;
import edu.npic.sps.features.parkingLot.ParkingLotRepository;
import edu.npic.sps.features.parkingSpace.dto.ParkingSpaceRequest;
import edu.npic.sps.features.parkingSpace.dto.ParkingSpaceResponse;
import edu.npic.sps.features.site.SiteRepository;
import edu.npic.sps.mapper.ParkingSpaceMapper;
import edu.npic.sps.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParkingSpaceServiceImpl implements ParkingSpaceService {
    private final SiteRepository siteRepository;

    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ParkingSpaceMapper parkingSpaceMapper;
    private final ParkingLotRepository parkingLotRepository;
    private final AuthUtil authUtil;

    @Override
    public Page<ParkingSpaceResponse> filter(int pageNo, int pageSize, String branchUuid, String keywords) {
        boolean isAdmin  = authUtil.isAdminLoggedUser();
        boolean isManager = authUtil.isManagerLoggedUser();
        List<String> sites = authUtil.loggedUserSites();

        if (pageNo < 1 || pageSize < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page number or page size must be greater than zero"
            );
        }

        List<String> branchUuids = null;
        if(branchUuid!=null && !branchUuid.isEmpty()){
            branchUuids = Arrays.stream(branchUuid.split(","))
                    .map(String::trim)
                    .toList();
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<ParkingSpace> parkingSpaces = Page.empty();
        if (isAdmin) {
            parkingSpaces = parkingSpaceRepository.filterParkingSpace(keywords, branchUuids, pageable);
        }else  if (isManager) {
            parkingSpaces = parkingSpaceRepository.filterParkingSpace(keywords, sites, pageable);
        }

        return parkingSpaces.map(parkingSpaceMapper::toParkingSpaceResponse);
    }

    @Override
    public ParkingSpaceResponse update(String uuid, ParkingSpaceRequest parkingSpaceRequest) {

        ParkingSpace parkingSpace = parkingSpaceRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking Space Not Found!")
        );

        if (parkingSpaceRequest.siteUuid() != null){
            Site site = siteRepository.findByUuid(parkingSpaceRequest.siteUuid()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found!")
            );
            parkingSpace.setSite(site);
        }

        parkingSpaceMapper.fromParkingSpaceRequest(parkingSpaceRequest, parkingSpace);

        List<ParkingLot> parkingLotList = parkingLotRepository.findAllLotByParkingSpaceUuid(uuid);

        parkingLotRepository.deleteAll(parkingLotList);

        parkingSpace.setLotQty(0);
        parkingSpace.setEmpty(0);

        ParkingSpace updatedParkingSpace = parkingSpaceRepository.save(parkingSpace);

        return parkingSpaceMapper.toParkingSpaceResponse(updatedParkingSpace);
    }

    @Override
    public void delete(String uuid) {

        ParkingSpace parkingSpace = parkingSpaceRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking space not found!")
        );

        parkingSpaceRepository.deleteByUuid(parkingSpace.getUuid());
    }

    @Override
    public ParkingSpaceResponse findByUuid(String uuid) {
        boolean isManager = authUtil.isManagerLoggedUser();
        List<String> sites = authUtil.loggedUserSites();

        if (!parkingSpaceRepository.existsBySite_UuidInAndUuid(sites, uuid) && isManager){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking space not found in this branch");
        }

        ParkingSpace parkingSpace = parkingSpaceRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Parking not found!")
        );

        return parkingSpaceMapper.toParkingSpaceResponse(parkingSpace);
    }

    @Override
    public Page<ParkingSpaceResponse> findAll(int pageNo, int pageSize) {
        boolean isManager = authUtil.isManagerLoggedUser();
        boolean isAdmin = authUtil.isAdminLoggedUser();
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

        if (isAdmin) {
           parkingSpaces = parkingSpaceRepository.findAll(pageRequest);
        }else if (isManager){
            parkingSpaces = parkingSpaceRepository.findParkingSpacesBySiteUuid(sites.stream().findFirst().orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Site not found!"
            )), pageRequest);
        }

        return parkingSpaces.map(parkingSpaceMapper::toParkingSpaceResponse);
    }

    @Override
    public List<ParkingNameResponse> getAllLabels() {
        boolean isManager = authUtil.isManagerLoggedUser();
        boolean isAdmin = authUtil.isAdminLoggedUser();

        List<String> sites = authUtil.loggedUserSites();

        List<ParkingSpace> parkingSpaceList = new ArrayList<>();

        Sort sort = Sort.by(Sort.Direction.DESC, "id");

        if (isAdmin) {
            parkingSpaceList = parkingSpaceRepository.findAll(sort);
        }
        else if (isManager){
            parkingSpaceList = parkingSpaceRepository.findBySiteUuid(sites.stream().findFirst().orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Site not found!"
            )));
        }

        return parkingSpaceMapper.toLabelResponse(parkingSpaceList);
    }

    @Override
    public ParkingSpaceResponse create(CreateParkingSpace createParkingSpace) {

        Site sites = siteRepository.findByUuid(createParkingSpace.siteUuid()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Site not found!")
        );

        ParkingSpace parkingSpace = parkingSpaceMapper.fromCreateParking(createParkingSpace);
        parkingSpace.setUuid(UUID.randomUUID().toString());
        parkingSpace.setIsDeleted(false);
        parkingSpace.setFilled(0);
        parkingSpace.setEmpty(0);
        parkingSpace.setLotQty(0);
        parkingSpace.setCreatedAt(LocalDateTime.now());
        parkingSpace.setSite(sites);
        parkingSpace.setParkingLots(new ArrayList<>());

        ParkingSpace savedParkingSpace = parkingSpaceRepository.save(parkingSpace);
        sites.setParkingSpacesQty(sites.getParkingSpacesQty() + 1);
        siteRepository.save(sites);

        return parkingSpaceMapper.toParkingSpaceResponse(savedParkingSpace);
    }

}
