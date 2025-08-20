package edu.npic.sps.features.vehicle;

import edu.npic.sps.base.Status;
import edu.npic.sps.domain.*;
import edu.npic.sps.features.gender.GenderRepository;
import edu.npic.sps.features.licensePlateProvince.LicensePlateProvinceRepository;
import edu.npic.sps.features.licensePlateType.LicensePlateTypeRepository;
import edu.npic.sps.features.parkingLotDetail.ParkingLotDetailRepository;
import edu.npic.sps.features.parkingLotDetail.dto.ParkingDetailResponse;
import edu.npic.sps.features.report.GenerateReportService;
import edu.npic.sps.features.role.RoleRepository;
import edu.npic.sps.features.signUpMethod.SignUpMethodRepository;
import edu.npic.sps.features.site.SiteRepository;
import edu.npic.sps.features.telegramBot.TelegramNotificationService;
import edu.npic.sps.features.user.UserRepository;
import edu.npic.sps.features.vehicle.dto.CameraRequest;
import edu.npic.sps.features.vehicle.dto.CreateVehicle;
import edu.npic.sps.features.vehicle.dto.VehicleRequest;
import edu.npic.sps.features.vehicle.dto.VehicleResponse;
import edu.npic.sps.features.vehicletype.VehicleTypeRepository;
import edu.npic.sps.mapper.ParkingLotDetailMapper;
import edu.npic.sps.mapper.VehicleMapper;
import edu.npic.sps.util.AuthUtil;
import edu.npic.sps.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

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
    private final ParkingLotDetailRepository parkingLotDetailRepository;
    private final ParkingLotDetailMapper parkingLotDetailMapper;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final PasswordEncoder passwordEncoder;
    private final GenderRepository genderRepository;
    private final SignUpMethodRepository signUpMethodRepository;
    private final RoleRepository roleRepository;
    private final TelegramNotificationService telegramNotificationService;
    private final GenerateReportService generateReportService;

    @Value("${vehicle.template.path}")
    String templatePath;

    @Value("${vehicle-excel.template.path}")
    String excelTemplatePath;

    @Override
    public ResponseEntity<InputStreamResource> getVehicleReportExcel() throws IOException {

        List<Vehicle> vehicles = vehicleRepository.findAll();
        File file = generateReportService.generateExcelReport(vehicles, excelTemplatePath);
        HttpHeaders headers = Util.getHttpHeaders("Vehicle", file, "xlsx", MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));

        return new ResponseEntity<>(new InputStreamResource(new FileInputStream(file)), headers, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<InputStreamResource> getVehicleReportPdf() throws IOException {

        List<Vehicle> vehicles = vehicleRepository.findAll();
        File file = generateReportService.generatePDFReport(vehicles, templatePath);
        HttpHeaders headers = Util.getHttpHeaders("Vehicle", file, "pdf", MediaType.APPLICATION_PDF);

        return new ResponseEntity<>(new InputStreamResource(new FileInputStream(file)), headers, HttpStatus.OK);
    }

    @Override
    public ParkingDetailResponse checkOut(CameraRequest cameraRequest) {

        Vehicle vehicle = vehicleRepository.findByNumberPlate(cameraRequest.numberPlate()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehicle not found!")
        );

        ParkingLotDetail parkingLotDetail = parkingLotDetailRepository.findByVehicle(true, vehicle.getUuid()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No active parking session found for this vehicle!"
        ));

        LocalDateTime timeIn = parkingLotDetail.getTimeIn();
        LocalDateTime timeOut = LocalDateTime.now();
        Long durations = Duration.between(timeIn, timeOut).toMinutes();

        parkingLotDetail.setIsParking(false);
        parkingLotDetail.setTimeOut(timeOut);
        parkingLotDetail.setDurations(durations);
        parkingLotDetail.setIsCheckOut(true);
        parkingLotDetail.setCreatedAt(LocalDateTime.now());
        parkingLotDetail.setImageCheckOut(cameraRequest.imageCheckOut());

        ParkingLotDetail savedParkingLotDetail = parkingLotDetailRepository.save(parkingLotDetail);

        simpMessagingTemplate.convertAndSend(
                "/topic/check-out",
                parkingLotDetailMapper.toParkingDetailResponse(savedParkingLotDetail)
        );

        simpMessagingTemplate.convertAndSend(
                "/topic/" + parkingLotDetail.getSite().getUuid() +"/check-out",
                parkingLotDetailMapper.toParkingDetailResponse(savedParkingLotDetail)
        );

        // 🔥 NEW: Send Telegram notification for check-out
        String provinceName = vehicle.getLicensePlateProvince() != null ?
                vehicle.getLicensePlateProvince().getProvinceNameEn() : null;

        telegramNotificationService.sendCheckOutNotification(
                vehicle.getNumberPlate(),
                provinceName,
                timeIn,
                timeOut,
                durations,
                savedParkingLotDetail.getImageCheckOut(),
                parkingLotDetail.getSite().getSiteName()
        );

        return parkingLotDetailMapper.toParkingDetailResponse(parkingLotDetail);
    }

    @Override
    public Page<ParkingDetailResponse> filterCheckOut(int pageNo, int pageSize, String keywords, LocalDateTime dateFrom, LocalDateTime dateTo) {
        boolean isManager = authUtil.isManagerLoggedUser();
        boolean isAdmin = authUtil.isAdminLoggedUser();
        List<String> siteUuid = authUtil.loggedUserSites();

        if (pageNo < 1 || pageSize < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page size must be greater than 1"
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<ParkingLotDetail> parkingLotDetailPage = Page.empty();
        if (isAdmin) {
            if (dateFrom != null && dateTo != null){
                parkingLotDetailPage = parkingLotDetailRepository.filterCheckOutWithDateRange(
                        keywords,
                        dateFrom,
                        dateTo,
                        pageRequest
                );
            }else {
                parkingLotDetailPage = parkingLotDetailRepository.filterCheckOutByKeywords(
                        keywords,
                        pageRequest
                );
            }
        }else if (isManager) {
            if (dateFrom != null && dateTo != null){
                parkingLotDetailPage = parkingLotDetailRepository.filterCheckOutWithDateRangeManager(
                        keywords,
                        dateFrom,
                        dateTo,
                        siteUuid.getFirst(),
                        pageRequest
                );
            }else {
                parkingLotDetailPage = parkingLotDetailRepository.filterCheckOutByKeywordsManager(
                        keywords,
                        siteUuid.getFirst(),
                        pageRequest
                );
            }
        }


        return parkingLotDetailPage.map(parkingLotDetailMapper::toParkingDetailResponse);

    }

    @Override
    public Page<ParkingDetailResponse> getAllCheckOut(int pageNo, int pageSize) {
        boolean isManager = authUtil.isManagerLoggedUser();
        boolean isAdmin = authUtil.isAdminLoggedUser();
        List<String> siteUuid = authUtil.loggedUserSites();

        if (pageNo < 1 || pageSize < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page size and page no must be greater than 1"
            );
        }
        Page<ParkingLotDetail> parkingLotDetails = Page.empty();

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        if (isAdmin) {
            parkingLotDetails = parkingLotDetailRepository.findByIsCheckOutTrue(pageRequest);
        }else if (isManager) {
            parkingLotDetails = parkingLotDetailRepository.findByIsCheckOutTrueAndSite_Uuid(siteUuid.getFirst(), pageRequest);
        }

        return parkingLotDetails.map(parkingLotDetailMapper::toParkingDetailResponse);

    }

    @Override
    public Page<ParkingDetailResponse> filterCheckIn(int pageNo, int pageSize, String keywords, LocalDateTime dateFrom, LocalDateTime dateTo) {

        boolean isManager = authUtil.isManagerLoggedUser();
        boolean isAdmin = authUtil.isAdminLoggedUser();
        List<String> siteUuid = authUtil.loggedUserSites();

        if (pageNo < 1 || pageSize < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page size must be greater than 1"
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<ParkingLotDetail> parkingLotDetailPage = Page.empty();

        if (isAdmin) {
            if (dateFrom != null && dateTo != null){
                parkingLotDetailPage = parkingLotDetailRepository.filterCheckInWithDateRange(
                        keywords,
                        dateFrom,
                        dateTo,
                        pageRequest
                );
            }else{
                parkingLotDetailPage = parkingLotDetailRepository.filterCheckInByKeywords(
                        keywords,
                        pageRequest
                );
            }
        }else if (isManager) {
            if (dateFrom != null && dateTo != null){
                parkingLotDetailPage = parkingLotDetailRepository.filterCheckInWithDateRangeManager(
                        keywords,
                        dateFrom,
                        dateTo,
                        siteUuid.getFirst(),
                        pageRequest
                );
            }else{
                parkingLotDetailPage = parkingLotDetailRepository.filterCheckInByKeywordsManager(
                        keywords,
                        siteUuid.getFirst(),
                        pageRequest
                );
            }
        }

        return parkingLotDetailPage.map(parkingLotDetailMapper::toParkingDetailResponse);

    }

    @Override
    public Page<ParkingDetailResponse> getAllCheckIn(int pageNo, int pageSize) {
        boolean isManager = authUtil.isManagerLoggedUser();
        boolean isAdmin = authUtil.isAdminLoggedUser();
        List<String> siteUuid = authUtil.loggedUserSites();

        if (pageNo < 1 || pageSize < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page size must be greater than 1"
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<ParkingLotDetail> parkingLotDetails = Page.empty();
        if (isAdmin) {
            parkingLotDetails = parkingLotDetailRepository.findByIsCheckInTrue(pageRequest);
        } else if (isManager) {
            parkingLotDetails = parkingLotDetailRepository.findByIsCheckInTrueAndSite_Uuid(siteUuid.getFirst(), pageRequest);
        }

        return parkingLotDetails.map(parkingLotDetailMapper::toParkingDetailResponse);

    }

    @Override
    public Page<VehicleResponse> getVehicleReport(int pageNo, int pageSize, LocalDateTime dateFrom, LocalDateTime dateTo) {
        if (pageNo < 1 || pageSize < 1){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page number and page size must be greater than 0!"
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Vehicle> vehicles = Page.empty();

        vehicles = vehicleRepository.findByCreatedAtBetween(
                dateFrom,
                dateTo, pageRequest);

        return vehicles.map(vehicleMapper::toVehicleResponse);

    }

    @Override
    public ParkingDetailResponse checkIn(CameraRequest cameraRequest) {

        Optional<Vehicle> vehicle = vehicleRepository.findByNumberPlate(cameraRequest.numberPlate());

        Optional<LicensePlateProvince> licensePlateProvince = licensePlateProvinceRepository.findByAliasIgnoreCase(cameraRequest.provincePlate().toLowerCase());

        Site site = siteRepository.findByUuid(cameraRequest.branchUuid()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found!")
        );

        Vehicle newVehicle = null;
        if (vehicle.isEmpty()){

            User user = new User();

            user.setFullName("Guest");
            user.setPassword(passwordEncoder.encode(UUID.randomUUID().toString().substring(0, 5)));

            // Assign specific email for the first user
            user.setEmail(UUID.randomUUID().toString()+"@gmail.com");

            user.setDateOfBirth(LocalDate.now().minusYears(20));
            user.setGender(genderRepository.findById(1).get());
            user.setPhoneNumber(UUID.randomUUID().toString().substring(0, 5));
            user.setUuid(UUID.randomUUID().toString());
            user.setCreatedAt(LocalDateTime.now());
            user.setIsVerified(false);
            user.setIsCredentialsNonExpired(true);
            user.setIsAccountNonExpired(true);
            user.setIsAccountNonLocked(true);
            user.setIsDeleted(true);
            user.setIsOnline(false);
            user.setSignUpMethod(signUpMethodRepository.findByName("CUSTOM").get());
            user.setIsTwoFactorEnabled(false);
            user.setStatus(String.valueOf(Status.Pending));

            List<Role> roles = new ArrayList<>();
            // Ensure roles exist
            roles.add(roleRepository.findByNameIgnoreCase("USER").orElseThrow(() -> new RuntimeException("Role User not found")));
            user.setSites(new ArrayList<>());
            user.setRoles(roles);

            User savedUser = userRepository.save(user);

            newVehicle = new Vehicle();
            newVehicle.setUuid(UUID.randomUUID().toString());
            newVehicle.setVehicleMake("N/A");
            newVehicle.setVehicleModel("N/A");
            newVehicle.setNumberPlate(cameraRequest.numberPlate());
            newVehicle.setLicensePlateProvince(licensePlateProvince.orElse(null));
            newVehicle.setColor("N/A");
            newVehicle.setSites(new ArrayList<>(List.of(site)));
            newVehicle.setIsDeleted(false);
            newVehicle.setCreatedAt(LocalDateTime.now());
            newVehicle.setUser(savedUser);
            vehicleRepository.save(newVehicle);
        }else if(parkingLotDetailRepository.existsByVehicle_UuidAndIsParking(vehicle.get().getUuid(), true)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle is parking!");
        }

        LocalDateTime checkInTime = LocalDateTime.now();

        ParkingLotDetail parkingLotDetail = new ParkingLotDetail();
        parkingLotDetail.setUuid(UUID.randomUUID().toString());
        parkingLotDetail.setIsParking(true);
        parkingLotDetail.setTimeIn(checkInTime);
        parkingLotDetail.setVehicle(vehicle.orElse(newVehicle));
        parkingLotDetail.setIsCheckIn(true);
        parkingLotDetail.setIsCheckOut(false);
        parkingLotDetail.setImage(cameraRequest.image());
        parkingLotDetail.setImageCheckIn(cameraRequest.image());
        parkingLotDetail.setSite(site);
        parkingLotDetail.setCreatedAt(checkInTime);
        ParkingLotDetail savedParkingLotDetail = parkingLotDetailRepository.save(parkingLotDetail);

        simpMessagingTemplate.convertAndSend(
                "/topic/check-in",
                parkingLotDetailMapper.toParkingDetailResponse(savedParkingLotDetail)
        );

        simpMessagingTemplate.convertAndSend(
                "/topic/" + site.getUuid() +"/check-in",
                parkingLotDetailMapper.toParkingDetailResponse(savedParkingLotDetail)
        );

        // 🔥 NEW: Send Telegram notification for check-in
        Vehicle finalVehicle = vehicle.orElse(newVehicle);
        String provinceName = finalVehicle.getLicensePlateProvince() != null ?
                finalVehicle.getLicensePlateProvince().getProvinceNameEn() :
                cameraRequest.provincePlate();

        telegramNotificationService.sendCheckInNotification(
                finalVehicle.getNumberPlate(),
                provinceName,
                checkInTime,
                savedParkingLotDetail.getImageCheckIn(),
                site.getSiteName()
        );

        return parkingLotDetailMapper.toParkingDetailResponse(parkingLotDetail);
    }



//    @Override
//    public ParkingDetailResponse checkIn(String numberPlate, String provincePlate, String vehicleModel, String vehicleMake, String color, String space, String lot) {
//
//        List<String> sites = authUtil.loggedUserSites();
//
//        Optional<Vehicle> vehicle = vehicleRepository.findByNumberPlate(numberPlate);
//
//        Optional<LicensePlateProvince> licensePlateProvince = licensePlateProvinceRepository.findByProvinceNameEnIgnoreCase(provincePlate);
//
//        ParkingSpace parkingSpace = parkingSpaceRepository.findByLabelIgnoreCase(space.trim()).orElseThrow(
//                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking space not found!")
//        );
//
//        if (parkingSpace.getEmpty() > 0){
//            parkingSpace.setEmpty(parkingSpace.getEmpty() - 1);
//            parkingSpace.setFilled(parkingSpace.getFilled() + 1);
//        }else {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking space is filled!");
//        }
//
//        ParkingLot parkingLot = parkingSpace.getParkingLots().stream().filter(
//                s -> s.getLotName().equalsIgnoreCase(lot)
//        ).findFirst().orElseThrow(
//                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking slot not found in the location = "+ parkingSpace.getLabel())
//        );
//
//        if (parkingLot.getIsAvailable().equals(false)){
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "Slot is not available!");
//        }
//
//        parkingLot.setIsAvailable(false);
//
//        if (!licensePlateProvince.isPresent()){
//            LicensePlateProvince newLicensePlateProvince = new LicensePlateProvince();
//            newLicensePlateProvince.setUuid(UUID.randomUUID().toString());
//            newLicensePlateProvince.setProvinceNameEn(provincePlate);
//            licensePlateProvinceRepository.save(newLicensePlateProvince);
//        }
//        Vehicle newVehicle = null;
//        if (vehicle.isEmpty()){
//            newVehicle = new Vehicle();
//            Site site = siteRepository.findByUuid(sites.stream().findFirst().orElseThrow()).orElseThrow(
//                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found!")
//            );
//            if (!sites.contains(site.getUuid())) {
//                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not authorized for site");
//            }
//            newVehicle.setUuid(UUID.randomUUID().toString());
//            newVehicle.setVehicleMake(vehicleMake);
//            newVehicle.setVehicleModel(vehicleModel);
//            newVehicle.setNumberPlate(numberPlate);
//            newVehicle.setLicensePlateProvince(licensePlateProvince.get());
//            newVehicle.setColor(color);
//            newVehicle.setIsDeleted(false);
//            newVehicle.setSites(List.of(site));
//            newVehicle.setCreatedAt(LocalDateTime.now());
//            vehicleRepository.save(newVehicle);
//        }else if(parkingLotDetailRepository.existsByVehicle_UuidAndIsParking(vehicle.get().getUuid(), true)){
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "Vehicle is parking!");
//        }
//
//        ParkingLotDetail parkingLotDetail = new ParkingLotDetail();
//        parkingLotDetail.setUuid(UUID.randomUUID().toString());
//        parkingLotDetail.setIsParking(true);
//        parkingLotDetail.setTimeIn(LocalDateTime.now());
//        parkingLotDetail.setParkingSpace(parkingSpace);
//        parkingLotDetail.setParkingLot(parkingLot);
//        parkingLotDetail.setVehicle(vehicle.orElse(newVehicle));
//        parkingLotDetail.setCreatedAt(LocalDateTime.now());
//        parkingLotDetailRepository.save(parkingLotDetail);
//
//        simpMessagingTemplate.convertAndSend(
//                "/topic/slot-update",
//                parkingLotDetailMapper.toParkingDetailResponse(parkingLotDetail)
//        );
//
//        return parkingLotDetailMapper.toParkingDetailResponse(parkingLotDetail);
//
//    }

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
