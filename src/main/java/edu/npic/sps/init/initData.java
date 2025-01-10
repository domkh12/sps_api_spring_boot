package edu.npic.sps.init;

import edu.npic.sps.base.Status;
import edu.npic.sps.domain.*;
import edu.npic.sps.features.city.CityRepository;
import edu.npic.sps.features.company.CompanyRepository;
import edu.npic.sps.features.siteType.SiteTypeRepository;
import edu.npic.sps.features.gender.GenderRepository;
import edu.npic.sps.features.parkingSpace.ParkingSpaceRepository;
import edu.npic.sps.features.parkingLot.ParkingLotRepository;
import edu.npic.sps.features.role.RoleRepository;
import edu.npic.sps.features.signUpMethod.SignUpMethodRepository;
import edu.npic.sps.features.site.SiteRepository;
import edu.npic.sps.features.user.UserRepository;
import edu.npic.sps.features.vehicle.VehicleRepository;
import edu.npic.sps.features.vehicletype.VehicleTypeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class initData {

    private final VehicleTypeRepository vehicleTypeRepository;
    private final VehicleRepository vehicleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ParkingSpaceRepository parkingSpaceRepository;
    private final ParkingLotRepository parkingLotRepository;
    private final SignUpMethodRepository signUpMethodRepository;
    private final GenderRepository genderRepository;
    private final CompanyRepository companyRepository;
    private final SiteRepository siteRepository;
    private final SiteTypeRepository siteTypeRepository;
    private final CityRepository cityRepository;

    @PostConstruct
    public void init() {
        try {
            initCityData();
            initSiteTypeData();
            initCompanyData();
            initSitesData();
            initVehicleTypeData();
            initGenderData();
            initRoles();
            initParkingSpacesData();
            initSignUpMethodData();
            initUsersData();
            initParkingLotsData();
            initVehicleData();
        } catch (Exception e) {
            System.err.println("Error during initialization: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initSiteTypeData(){
        SiteType siteType = new SiteType();
        siteType.setUuid(UUID.randomUUID().toString());
        siteType.setName("Education");
        siteTypeRepository.save(siteType);
    }

    private void initCityData(){
        City city = new City();
        city.setUuid(UUID.randomUUID().toString());
        city.setName("Phnom Penh");
        cityRepository.save(city);
    }

    private void initSitesData(){
        Site site1 = new Site();
        Company company = companyRepository.findById(1).get();
        Company company1 = companyRepository.findById(2).get();
        site1.setUuid(UUID.randomUUID().toString());
        site1.setSiteName("Npic1");
        site1.setCompany(company);
        site1.setSiteType(siteTypeRepository.findById(1).orElseThrow());
        site1.setCreatedAt(LocalDateTime.now());
        site1.setSiteAddress("123 Main St");
        site1.setCity(cityRepository.findById(1).orElseThrow());

        Site site2 = new Site();
        site2.setUuid(UUID.randomUUID().toString());
        site2.setSiteName("Npic2");
        site2.setCompany(company);
        site2.setSiteType(siteTypeRepository.findById(1).orElseThrow());
        site2.setCity(cityRepository.findById(1).orElseThrow());
        site2.setSiteAddress("124 Main St");
        site2.setCreatedAt(LocalDateTime.now());

        Site site3 = new Site();
        site3.setUuid(UUID.randomUUID().toString());
        site3.setSiteName("Npic3");
        site3.setCompany(company1);
        site3.setSiteAddress("124 Main St");
        site2.setCity(cityRepository.findById(1).orElseThrow());
        site3.setSiteType(siteTypeRepository.findById(1).orElseThrow());
        site3.setCreatedAt(LocalDateTime.now());

        siteRepository.saveAll(List.of(site1, site2, site3));
    }

    private void initCompanyData(){
        Company company = new Company();
        company.setUuid(UUID.randomUUID().toString());
        company.setCompanyName("National Polytechnic Institute of Cambodia");
        company.setCreatedAt(LocalDateTime.now());
        companyRepository.save(company);

        Company company1 = new Company();
        company1.setUuid(UUID.randomUUID().toString());
        company1.setCompanyName("RUPP");
        company1.setCreatedAt(LocalDateTime.now());
        companyRepository.save(company1);
    }

    private void initGenderData(){
        Gender male = new Gender();
        male.setUuid(UUID.randomUUID().toString());
        male.setGender("Male");
        Gender female = new Gender();
        female.setUuid(UUID.randomUUID().toString());
        female.setGender("Female");
        genderRepository.saveAll(List.of(male, female));
    }

    private void initSignUpMethodData(){
        SignUpMethod signUpMethod = new SignUpMethod();
        signUpMethod.setUuid(UUID.randomUUID().toString());
        signUpMethod.setName("AZURE");
        SignUpMethod signUpMethod2 = new SignUpMethod();
        signUpMethod2.setUuid(UUID.randomUUID().toString());
        signUpMethod2.setName("CUSTOM");
        signUpMethodRepository.save(signUpMethod2);
        signUpMethodRepository.save(signUpMethod);
    }

    private void initParkingLotsData() {
        ParkingLot parkingLot1 = new ParkingLot();
        ParkingSpace parkingSpace1 = parkingSpaceRepository.findById(1).orElseThrow();

        parkingLot1.setUuid(UUID.randomUUID().toString());
        parkingLot1.setLotName("P1-L1");
        parkingLot1.setParkingSpace(parkingSpace1);
        parkingLot1.setIsDeleted(false);
        parkingLot1.setIsAvailable(true);

        parkingSpace1.setEmpty(parkingSpace1.getEmpty() + 1);
        parkingSpace1.setLotQty(parkingSpace1.getLotQty() + 1);
        parkingSpaceRepository.save(parkingSpace1);
        parkingLotRepository.save(parkingLot1);

        ParkingLot parkingLot2 = new ParkingLot();
        ParkingSpace parkingSpace2 = parkingSpaceRepository.findById(2).orElseThrow();

        parkingLot2.setUuid(UUID.randomUUID().toString());
        parkingLot2.setLotName("P2-L1");
        parkingLot2.setParkingSpace(parkingSpace2);
        parkingLot2.setIsDeleted(false);
        parkingLot2.setIsAvailable(true);

        parkingSpace2.setEmpty(parkingSpace2.getEmpty() + 1);
        parkingSpace2.setLotQty(parkingSpace2.getLotQty() + 1);
        parkingSpaceRepository.save(parkingSpace2);
        parkingLotRepository.save(parkingLot2);
    }

    private void initParkingSpacesData(){

        ParkingSpace parkingSpace1 = new ParkingSpace();
        parkingSpace1.setUuid(UUID.randomUUID().toString());
        parkingSpace1.setLabel("P1");
        parkingSpace1.setLotQty(0);
        parkingSpace1.setFilled(0);
        parkingSpace1.setEmpty(0);
        parkingSpace1.setCreatedAt(LocalDateTime.now());
        parkingSpace1.setIsDeleted(false);
        parkingSpace1.setSite(siteRepository.findBySiteNameIgnoreCase("npic1").stream().findFirst().orElseThrow());
        parkingSpaceRepository.save(parkingSpace1);


        ParkingSpace parkingSpace2 = new ParkingSpace();
        parkingSpace2.setUuid(UUID.randomUUID().toString());
        parkingSpace2.setLabel("P2");
        parkingSpace2.setLotQty(0);
        parkingSpace2.setFilled(0);
        parkingSpace2.setEmpty(0);
        parkingSpace2.setCreatedAt(LocalDateTime.now());
        parkingSpace2.setIsDeleted(false);
        parkingSpace2.setSite(siteRepository.findBySiteNameIgnoreCase("npic2").stream().findFirst().orElseThrow());
        parkingSpaceRepository.save(parkingSpace2);
    }

    private void initUsersData() {
        User user = new User();
        List<Site> sites= List.of(
                siteRepository.findBySiteNameIgnoreCase("npic2").orElseThrow()
        );

        user.setFullName("NPIC");
        user.setPassword(passwordEncoder.encode("Npic@2024"));

        // Assign specific email for the first user
        user.setEmail("npic@gmail.com");

        user.setDateOfBirth(LocalDate.now().minusYears(20));
        user.setGender(genderRepository.findById(1).get());
        user.setPhoneNumber("0877345470");
        user.setUuid(UUID.randomUUID().toString());
        user.setCreatedAt(LocalDateTime.now());
        user.setIsVerified(true);
        user.setIsCredentialsNonExpired(true);
        user.setIsAccountNonExpired(true);
        user.setIsAccountNonLocked(true);
        user.setIsDeleted(false);
        user.setIsOnline(false);
        user.setSignUpMethod(signUpMethodRepository.findByName("CUSTOM").get());
        user.setIsTwoFactorEnabled(false);
        user.setStatus(String.valueOf(Status.Active));

        List<Role> roles = new ArrayList<>();
        // Ensure roles exist
        roles.add(roleRepository.findById(1).orElseThrow(() -> new RuntimeException("Role 1 not found")));
        roles.add(roleRepository.findById(2).orElseThrow(() -> new RuntimeException("Role 2 not found")));
        roles.add(roleRepository.findById(3).orElseThrow(() -> new RuntimeException("Role 3 not found")));
        user.setSites(sites);
        user.setRoles(roles);

        userRepository.save(user);

        User user1 = new User();
        List<Site> sites1= List.of(
                siteRepository.findBySiteNameIgnoreCase("npic1").orElseThrow(),
                siteRepository.findBySiteNameIgnoreCase("npic2").orElseThrow()
        );
        log.info("site : {}",sites1);
        user1.setFullName("admin007");
        user1.setPassword(passwordEncoder.encode("Npic@2025"));

        // Assign specific email for the first user
        user1.setEmail("admin@gmail.com");
        user1.setGender(genderRepository.findById(1).get());
        user1.setDateOfBirth(LocalDate.now().minusYears(20));
        user1.setPhoneNumber("0877345471");
        user1.setUuid(UUID.randomUUID().toString());
        user1.setCreatedAt(LocalDateTime.now());
        user1.setIsVerified(true);
        user1.setIsCredentialsNonExpired(true);
        user1.setIsAccountNonExpired(true);
        user1.setIsAccountNonLocked(true);
        user1.setIsDeleted(false);
        user1.setIsOnline(false);
        user1.setSignUpMethod(signUpMethodRepository.findByName("CUSTOM").get());
        user1.setIsTwoFactorEnabled(false);
        user1.setStatus(String.valueOf(Status.Active));

        List<Role> roles1 = new ArrayList<>();
        // Ensure roles exist
        roles1.add(roleRepository.findById(2).orElseThrow(() -> new RuntimeException("Role 2 not found")));
        roles1.add(roleRepository.findById(3).orElseThrow(() -> new RuntimeException("Role 3 not found")));
        user1.setSites(sites1);
        user1.setRoles(roles1);

        userRepository.save(user1);

        User user2 = new User();
        List<Site> sites2 = List.of(
                siteRepository.findBySiteNameIgnoreCase("npic1").orElseThrow()
        );
        log.info("site : {}",sites2);
        user2.setFullName("Admin008");
        user2.setPassword(passwordEncoder.encode("Npic@2025"));
        user2.setGender(genderRepository.findById(1).get());
        // Assign specific email for the first user
        user2.setEmail("admin1@gmail.com");

        user2.setDateOfBirth(LocalDate.now().minusYears(20));
        user2.setPhoneNumber("0877345473");
        user2.setUuid(UUID.randomUUID().toString());
        user2.setCreatedAt(LocalDateTime.now());
        user2.setIsVerified(true);
        user2.setIsCredentialsNonExpired(true);
        user2.setIsAccountNonExpired(true);
        user2.setIsAccountNonLocked(true);
        user2.setIsDeleted(false);
        user2.setIsOnline(false);
        user2.setSignUpMethod(signUpMethodRepository.findByName("CUSTOM").get());
        user2.setIsTwoFactorEnabled(false);
        user2.setStatus(String.valueOf(Status.Active));

        List<Role> roles2 = new ArrayList<>();
        // Ensure roles exist
        roles2.add(roleRepository.findById(2).orElseThrow(() -> new RuntimeException("Role 2 not found")));

        user2.setSites(sites2);
        user2.setRoles(roles2);

        userRepository.save(user2);

        User user3 = new User();
        List<Site> sites3 = List.of(
                siteRepository.findBySiteNameIgnoreCase("npic1").orElseThrow()
        );
        log.info("site : {}",sites3);
        user3.setFullName("panha");
        user3.setPassword(passwordEncoder.encode("Npic@2025"));
        user3.setGender(genderRepository.findById(1).get());
        // Assign specific email for the first user
        user3.setEmail("user@gmail.com");

        user3.setDateOfBirth(LocalDate.now().minusYears(20));
        user3.setPhoneNumber("087734544");
        user3.setUuid(UUID.randomUUID().toString());
        user3.setCreatedAt(LocalDateTime.now());
        user3.setIsVerified(true);
        user3.setIsCredentialsNonExpired(true);
        user3.setIsAccountNonExpired(true);
        user3.setIsAccountNonLocked(true);
        user3.setIsDeleted(false);
        user3.setIsOnline(false);
        user3.setSignUpMethod(signUpMethodRepository.findByName("CUSTOM").get());
        user3.setIsTwoFactorEnabled(false);
        user3.setStatus(String.valueOf(Status.Active));

        List<Role> roles3 = new ArrayList<>();
        // Ensure roles exist
        roles3.add(roleRepository.findById(3).orElseThrow(() -> new RuntimeException("Role 3 not found")));

        user3.setSites(sites3);
        user3.setRoles(roles3);

        userRepository.save(user3);
    }

    private void initRoles(){
        List<Role> roles = new ArrayList<>();
        roles.add(Role.builder().uuid(UUID.randomUUID().toString()).name("MANAGER").build());
        roles.add(Role.builder().uuid(UUID.randomUUID().toString()).name("ADMIN").build());
        roles.add(Role.builder().uuid(UUID.randomUUID().toString()).name("USER").build());

        roleRepository.saveAll(roles);

    }

    private void initVehicleTypeData(){
        VehicleType vehicleType1 = new VehicleType();
        vehicleType1.setId(1);
        vehicleType1.setName("Suv");
        vehicleType1.setAlias("suv");
        vehicleType1.setUuid(UUID.randomUUID().toString());

        VehicleType vehicleType2 = new VehicleType();
        vehicleType2.setId(2);
        vehicleType2.setName("Sports Car");
        vehicleType2.setAlias("sports-car");
        vehicleType2.setUuid(UUID.randomUUID().toString());

        VehicleType vehicleType3 = new VehicleType();
        vehicleType3.setId(3);
        vehicleType3.setName("Hybrid Cars");
        vehicleType3.setAlias("hybrid-cars");
        vehicleType3.setUuid(UUID.randomUUID().toString());

        VehicleType vehicleType4 = new VehicleType();
        vehicleType4.setId(4);
        vehicleType4.setName("Electric Cars");
        vehicleType4.setAlias("electric-cars");
        vehicleType4.setUuid(UUID.randomUUID().toString());

        VehicleType vehicleType5 = new VehicleType();
        vehicleType5.setId(5);
        vehicleType5.setName("Trucks");
        vehicleType5.setAlias("trucks");
        vehicleType5.setUuid(UUID.randomUUID().toString());
        vehicleTypeRepository.saveAll(List.of(vehicleType1, vehicleType2, vehicleType3, vehicleType4, vehicleType5));
    }

    private void initVehicleData() {
        VehicleType vehicleType = vehicleTypeRepository.findById(1).orElseThrow();
        Vehicle vehicle = new Vehicle();

        vehicle.setUuid(UUID.randomUUID().toString());
        vehicle.setVehicleModel("Model ");
        vehicle.setNumberPlate("1A-0000");
        vehicle.setLicensePlateKhName("ភ្នំពេញ");
        vehicle.setLicensePlateEngName("Phnom Penh");
        vehicle.setVehicleMake("Make");
        vehicle.setColor("#000000");
        vehicle.setVehicleType(vehicleType);
        vehicle.setIsDeleted(false);
        vehicle.setCreatedAt(LocalDateTime.now());
        vehicle.setVehicleDescription("This is car ");
        vehicle.setUser(userRepository.findById(1).orElseThrow());
        vehicle.setSites(List.of(siteRepository.findBySiteNameIgnoreCase("npic1").orElseThrow()));

        vehicleRepository.save(vehicle);

        VehicleType vehicleType1 = vehicleTypeRepository.findById(1).orElseThrow();
        Vehicle vehicle1 = new Vehicle();

        vehicle1.setUuid(UUID.randomUUID().toString());
        vehicle1.setVehicleModel("Model ");
        vehicle1.setNumberPlate("1A-0001");
        vehicle1.setLicensePlateKhName("ភ្នំពេញ");
        vehicle1.setLicensePlateEngName("Phnom Penh");
        vehicle1.setVehicleMake("Make");
        vehicle1.setColor("#000000");
        vehicle1.setVehicleType(vehicleType1);
        vehicle1.setIsDeleted(false);
        vehicle1.setCreatedAt(LocalDateTime.now());
        vehicle1.setVehicleDescription("This is car ");
        vehicle1.setUser(userRepository.findById(1).orElseThrow());
        vehicle1.setSites(List.of(siteRepository.findBySiteNameIgnoreCase("npic2").orElseThrow()));

        vehicleRepository.save(vehicle1);
    }

}
