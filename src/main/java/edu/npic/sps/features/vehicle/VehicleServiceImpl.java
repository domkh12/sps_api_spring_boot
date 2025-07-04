package edu.npic.sps.features.vehicle;

import edu.npic.sps.domain.*;
import edu.npic.sps.features.licensePlateProvince.LicensePlateProvinceRepository;
import edu.npic.sps.features.licensePlateType.LicensePlateTypeRepository;
import edu.npic.sps.features.parkingLot.ParkingLotRepository;
import edu.npic.sps.features.parkingLotDetail.ParkingLotDetailRepository;
import edu.npic.sps.features.parkingLotDetail.dto.ParkingDetailResponse;
import edu.npic.sps.features.parkingSpace.ParkingSpaceRepository;
import edu.npic.sps.features.site.SiteRepository;
import edu.npic.sps.features.user.UserRepository;
import edu.npic.sps.features.user.dto.UserDetailResponse;
import edu.npic.sps.features.vehicle.dto.CreateVehicle;
import edu.npic.sps.features.vehicle.dto.VehicleRequest;
import edu.npic.sps.features.vehicle.dto.VehicleResponse;
import edu.npic.sps.features.vehicletype.VehicleTypeRepository;
import edu.npic.sps.mapper.ParkingLotDetailMapper;
import edu.npic.sps.mapper.UserMapper;
import edu.npic.sps.mapper.VehicleMapper;
import edu.npic.sps.mapper.VehicleTypeMapper;
import edu.npic.sps.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleServiceImpl implements VehicleService{

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final UserRepository userRepository;
    private final VehicleTypeRepository vehicleTypeRepository;
    private final AuthUtil authUtil;
    private final LicensePlateTypeRepository licensePlateTypeRepository;
    private final LicensePlateProvinceRepository licensePlateProvinceRepository;
    private final SiteRepository siteRepository;
    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ParkingLotDetailRepository parkingLotDetailRepository;
    private final ParkingLotDetailMapper parkingLotDetailMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ParkingLotRepository parkingLotRepository;

    @Override
    public ParkingDetailResponse checkOut(String numberPlate, String vehicleModel, String vehicleMake, String color) {

        Vehicle vehicle = vehicleRepository.findByNumberPlate(numberPlate).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found!")
        );

        ParkingLotDetail parkingLotDetail = parkingLotDetailRepository.findByVehicle(true, vehicle.getUuid()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No active parking session found for this vehicle!"
        ));

        ParkingSpace parkingSpace = parkingLotDetail.getParkingSpace();
        ParkingLot parkingLot = parkingLotDetail.getParkingLot();

        parkingSpace.setEmpty(parkingSpace.getEmpty() + 1);
        parkingSpace.setFilled(parkingSpace.getFilled() - 1);
        parkingLot.setIsAvailable(true);
        parkingSpaceRepository.save(parkingSpace);
        parkingLotRepository.save(parkingLot);

        LocalDateTime timeIn = parkingLotDetail.getTimeIn();
        LocalDateTime timeOut = LocalDateTime.now();
        Long durationHours = java.time.Duration.between(timeIn, timeOut).toHours();

        parkingLotDetail.setIsParking(false);
        parkingLotDetail.setTimeOut(timeOut);
        parkingLotDetail.setDurationHours(durationHours);

        simpMessagingTemplate.convertAndSend(
                "/topic/slot-update",
                parkingLotDetailMapper.toParkingDetailResponse(parkingLotDetail)
        );
        parkingLotDetailRepository.save(parkingLotDetail);

        return parkingLotDetailMapper.toParkingDetailResponse(parkingLotDetail);
    }

    @Override
    public ParkingDetailResponse checkIn(String numberPlate, String provincePlate, String vehicleModel, String vehicleMake, String color, String space, String lot) {

        List<String> sites = authUtil.loggedUserSites();

        Optional<Vehicle> vehicle = vehicleRepository.findByNumberPlate(numberPlate);

        Optional<LicensePlateProvince> licensePlateProvince = licensePlateProvinceRepository.findByProvinceNameEnIgnoreCase(provincePlate);
        ParkingSpace parkingSpace = parkingSpaceRepository.findByLabelIgnoreCase(space).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking space not found!")
        );

        if (parkingSpace.getEmpty() > 0){
            parkingSpace.setEmpty(parkingSpace.getEmpty() - 1);
            parkingSpace.setFilled(parkingSpace.getFilled() + 1);
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking space is filled!");
        }

        ParkingLot parkingLot = parkingSpace.getParkingLots().stream().filter(
                s -> s.getLotName().equalsIgnoreCase(lot)
        ).findFirst().orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking slot not found in the location = "+ parkingSpace.getLabel())
        );

        if (parkingLot.getIsAvailable().equals(false)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slot is not available!");
        }

        parkingLot.setIsAvailable(false);

        if (!licensePlateProvince.isPresent()){
            LicensePlateProvince newLicensePlateProvince = new LicensePlateProvince();
            newLicensePlateProvince.setUuid(UUID.randomUUID().toString());
            newLicensePlateProvince.setProvinceNameEn(provincePlate);
            licensePlateProvinceRepository.save(newLicensePlateProvince);
        }
        Vehicle newVehicle = null;
        if (vehicle.isEmpty()){
            newVehicle = new Vehicle();
            Site site = siteRepository.findByUuid(sites.stream().findFirst().orElseThrow()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found!")
            );
            if (!sites.contains(site.getUuid())) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized for site");
            }
            newVehicle.setUuid(UUID.randomUUID().toString());
            newVehicle.setVehicleMake(vehicleMake);
            newVehicle.setVehicleModel(vehicleModel);
            newVehicle.setNumberPlate(numberPlate);
            newVehicle.setLicensePlateProvince(licensePlateProvince.get());
            newVehicle.setColor(color);
            newVehicle.setIsDeleted(false);
            newVehicle.setSites(List.of(site));
            newVehicle.setCreatedAt(LocalDateTime.now());
            vehicleRepository.save(newVehicle);
        }else if(parkingLotDetailRepository.existsByVehicle_UuidAndIsParking(vehicle.get().getUuid(), true)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle is parking!");
        }

        ParkingLotDetail parkingLotDetail = new ParkingLotDetail();
        parkingLotDetail.setUuid(UUID.randomUUID().toString());
        parkingLotDetail.setIsParking(true);
        parkingLotDetail.setTimeIn(LocalDateTime.now());
        parkingLotDetail.setParkingSpace(parkingSpace);
        parkingLotDetail.setParkingLot(parkingLot);
        parkingLotDetail.setVehicle(vehicle.orElse(newVehicle));
        parkingLotDetail.setCreatedAt(LocalDateTime.now());
        parkingLotDetailRepository.save(parkingLotDetail);

        simpMessagingTemplate.convertAndSend(
                "/topic/slot-update",
                parkingLotDetailMapper.toParkingDetailResponse(parkingLotDetail)
        );

        return parkingLotDetailMapper.toParkingDetailResponse(parkingLotDetail);

    }

    @Override
    public VehicleResponse getVehicleByUuid(String uuid) {
        boolean isManager = authUtil.isManagerLoggedUser();
        List<String> siteUuid = authUtil.loggedUserSites();

        if (!vehicleRepository.existsBySites_UuidInAndUuid(siteUuid, uuid) && isManager){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found in this branch");
        }

        Vehicle vehicle = vehicleRepository.findByUuid(uuid).orElseThrow(
                   () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found!")
        );

        return vehicleMapper.toVehicleResponse(vehicle);
    }

    @Override
    public VehicleResponse update(String uuid, VehicleRequest vehicleRequest) {

        Vehicle vehicle  = vehicleRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found!")
        );

        if(vehicleRepository.existsByNumberPlate(vehicleRequest.numberPlate()) && !vehicle.getNumberPlate().equals(vehicleRequest.numberPlate())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Vehicle number plate already exists"
            );
        }

        if (vehicleRequest.userId() != null){
            User user = userRepository.findByUuid(vehicleRequest.userId()).orElseThrow(
                    ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!")
            );
            vehicle.setUser(user);
        }

        if (vehicleRequest.vehicleTypeId() != null){
            VehicleType vehicleType = vehicleTypeRepository.findByUuid(vehicleRequest.vehicleTypeId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "VehicleType Not Found!")
            );
            vehicle.setVehicleType(vehicleType);
        }

        if (vehicleRequest.licensePlateTypeId() != null){
            LicensePlateType licensePlateType = licensePlateTypeRepository.findByUuid(vehicleRequest.licensePlateTypeId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "LicensePlateType Not Found")
            );
            vehicle.setLicensePlateType(licensePlateType);
        }

        if (vehicleRequest.licensePlateProvinceId() != null){
            LicensePlateProvince licensePlateProvince = licensePlateProvinceRepository.findByUuid(vehicleRequest.licensePlateProvinceId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "LicensePlateProvince Not Found")
            );
            vehicle.setLicensePlateProvince(licensePlateProvince);
        }

        if (vehicleRequest.branchUuid() != null){
            Site site = siteRepository.findByUuid(vehicleRequest.branchUuid()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found!")
            );
            vehicle.setSites(new ArrayList<>(List.of(site)));
        }

        vehicleMapper.fromVehicleRequest(vehicleRequest, vehicle);
        vehicle = vehicleRepository.save(vehicle);

        return vehicleMapper.toVehicleResponse(vehicle);
    }

    @Override
    public VehicleResponse create(CreateVehicle createVehicle) {

        List<String> verifiedUuid = authUtil.loggedUserSites();

        if (vehicleRepository.existsByNumberPlate(createVehicle.numberPlate())){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Vehicle number plate already exits");
        }

        User user = userRepository.findByUuid(createVehicle.userId()).orElseThrow(
                ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
        );

        VehicleType vehicleType = vehicleTypeRepository.findByUuid(createVehicle.vehicleTypeId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "VehicleType Not Found")
        );

        LicensePlateType licensePlateType = licensePlateTypeRepository.findByUuid(createVehicle.licensePlateTypeId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "LicensePlateType Not Found")
        );

        LicensePlateProvince licensePlateProvince = licensePlateProvinceRepository.findByUuid(createVehicle.licensePlateProvinceId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "LicensePlateProvince Not Found")
        );

        Site site = siteRepository.findByUuid(createVehicle.branchUuid()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found!")
        );

        Vehicle vehicle = vehicleMapper.fromCreateVehicle(createVehicle);
        vehicle.setUuid(UUID.randomUUID().toString());
        vehicle.setSites(List.of(site));
        vehicle.setUser(user);
        vehicle.setLicensePlateType(licensePlateType);
        vehicle.setLicensePlateProvince(licensePlateProvince);
        vehicle.setVehicleType(vehicleType);
        vehicle.setIsDeleted(false);
        vehicle.setCreatedAt(LocalDateTime.now());
        vehicleRepository.save(vehicle);

        return vehicleMapper.toVehicleResponse(vehicle);
    }

    @Override
    public VehicleResponse findByNumPlate(String numberPlate) {

        Vehicle vehicle = vehicleRepository.findByNumberPlate(numberPlate).orElseThrow(
                () -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "NumberPlate not found!"
                )
        );
        return vehicleMapper.toVehicleResponse(vehicle);
    }

    @Override
    public Page<VehicleResponse> findAll(int pageNo, int pageSize) {
        boolean isManager = authUtil.isManagerLoggedUser();
        boolean isAdmin = authUtil.isAdminLoggedUser();
        List<String> sites = authUtil.loggedUserSites();

        if (pageNo < 1){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page number must be greater than 0!"
            );
        }

        if (pageSize < 1){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page size must be greater than 0!"
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Vehicle> vehicles = Page.empty();

        if(isAdmin){
            vehicles = vehicleRepository.findAll(pageRequest);
        }else if(isManager){
            vehicles = vehicleRepository.findBySites_Uuid(sites.stream().findFirst().orElseThrow(), pageRequest);
        }

        return vehicles.map(vehicleMapper::toVehicleResponse);
    }

    @Override
    public void delete(String uuid) {
        Vehicle vehicle = vehicleRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found")
        );

        vehicleRepository.delete(vehicle);
    }

    @Override
    public Page<VehicleResponse> filter(int pageNo, int pageSize, String keywords, String vehicleTypeId, String branchId) {
        boolean isManager = authUtil.isManagerLoggedUser();
        boolean isAdmin = authUtil.isAdminLoggedUser();
        List<String> siteUuid = authUtil.loggedUserSites();

        if (pageNo < 1){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page number must be greater than 0!"
            );
        }

        if (pageSize < 1){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page size must be greater than 0!"
            );
        }

        List<String> branchIds = null;
        if(branchId!=null && !branchId.isEmpty()){
            branchIds = Arrays.stream(branchId.split(","))
                    .map(String::trim)
                    .toList();
        }

        List<String> vehicleTypeIds = null;
        if(vehicleTypeId!=null && !vehicleTypeId.isEmpty()){
            vehicleTypeIds = Arrays.stream(vehicleTypeId.split(","))
                    .map(String::trim)
                    .toList();
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Vehicle> vehicles = Page.empty();
        if(isAdmin){
            vehicles = vehicleRepository.filterVehicles(keywords, vehicleTypeIds, branchIds, pageRequest);
        }else if (isManager){
            vehicles = vehicleRepository.filterVehicles(keywords, vehicleTypeIds, siteUuid, pageRequest);
        }

        return vehicles.map(vehicleMapper::toVehicleResponse);
    }
}
