package edu.npic.sps.init;

import edu.npic.sps.base.Status;
import edu.npic.sps.domain.*;
import edu.npic.sps.features.city.CityRepository;
import edu.npic.sps.features.company.CompanyRepository;
import edu.npic.sps.features.companyType.CompanyTypeRepository;
import edu.npic.sps.features.licensePlateProvince.LicensePlateProvinceRepository;
import edu.npic.sps.features.licensePlateType.LicensePlateTypeRepository;
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
    private final LicensePlateTypeRepository licensePlateTypeRepository;
    private final LicensePlateProvinceRepository licensePlateProvinceRepository;
    private final CompanyTypeRepository companyTypeRepository;

//    @PostConstruct
    public void init() {
        try {
            initCompanyTypeData();
            initLicensePlateProvinces();
            initLicensePlateTypeData();
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
            System.err.println("Error during initializations: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initCompanyTypeData(){
        CompanyType companyType = new CompanyType();
        companyType.setUuid(UUID.randomUUID().toString());
        companyType.setName("Education");
        companyType.setCreatedAt(LocalDateTime.now());
        companyTypeRepository.save(companyType);
    }

    private void initSiteTypeData(){
        SiteType siteType = new SiteType();
        siteType.setUuid(UUID.randomUUID().toString());
        siteType.setName("Education");
        siteType.setCreatedAt(LocalDateTime.now());
        siteTypeRepository.save(siteType);
    }

    private void initCityData(){
        City city = new City();
        city.setUuid(UUID.randomUUID().toString());
        city.setName("Phnom Penh");
        city.setCreatedAt(LocalDateTime.now());
        cityRepository.save(city);
    }

    private void initLicensePlateTypeData(){
        LicensePlateType licensePlateType = new LicensePlateType();
        licensePlateType.setUuid(UUID.randomUUID().toString());
        licensePlateType.setName("Royal Cambodian Armed Forces");
        licensePlateType.setCreatedAt(LocalDateTime.now());
        licensePlateTypeRepository.save(licensePlateType);
    }

    private void initLicensePlateProvinces(){
        LicensePlateProvince licensePlateProvince = new LicensePlateProvince();
        licensePlateProvince.setUuid(UUID.randomUUID().toString());
        licensePlateProvince.setProvinceNameEn("Phnom Penh");
        licensePlateProvince.setProvinceNameKh("ភ្នំពេញ");
        licensePlateProvince.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince);
    }

    private void initSitesData(){
        Site site1 = new Site();
        Company company = companyRepository.findById(1).get();
        site1.setUuid(UUID.randomUUID().toString());
        site1.setSiteName("Npic1");
        site1.setCompany(company);
        site1.setSiteType(siteTypeRepository.findById(1).orElseThrow());
        site1.setCreatedAt(LocalDateTime.now());
        site1.setSiteAddress("123 Main St");
        site1.setParkingSpacesQty(0);
        site1.setCity(cityRepository.findById(1).orElseThrow());
        company.setSiteQty(company.getSiteQty() + 1);
        companyRepository.save(company);
        siteRepository.save(site1);
    }

    private void initCompanyData(){
        Company company = new Company();
        company.setUuid(UUID.randomUUID().toString());
        company.setCompanyName("National Polytechnic Institute of Cambodia");
        company.setCompanyAddress("123 Main St");
        company.setSiteQty(0);
        company.setCity(cityRepository.findById(1).orElseThrow());
        company.setCompanyType(companyTypeRepository.findById(1).orElseThrow());
        company.setCreatedAt(LocalDateTime.now());
        companyRepository.save(company);
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
        signUpMethod.setCreatedAt(LocalDateTime.now());
        SignUpMethod signUpMethod2 = new SignUpMethod();
        signUpMethod2.setUuid(UUID.randomUUID().toString());
        signUpMethod2.setName("CUSTOM");
        signUpMethod2.setCreatedAt(LocalDateTime.now());
        signUpMethodRepository.save(signUpMethod2);
        signUpMethodRepository.save(signUpMethod);
    }

    private void initParkingLotsData() {

    }

    private void initParkingSpacesData(){


    }

    private void initUsersData() {
        User user = new User();
        List<Site> sites= List.of(
                siteRepository.findBySiteNameIgnoreCase("npic1").orElseThrow()
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
        user.setSites(sites);
        user.setRoles(roles);

        userRepository.save(user);

    }

    private void initRoles(){
        List<Role> roles = new ArrayList<>();
        roles.add(Role.builder().uuid(UUID.randomUUID().toString()).name("MANAGER").createdAt(LocalDateTime.now()).build());
        roles.add(Role.builder().uuid(UUID.randomUUID().toString()).name("ADMIN").createdAt(LocalDateTime.now()).build());
        roles.add(Role.builder().uuid(UUID.randomUUID().toString()).name("USER").createdAt(LocalDateTime.now()).build());
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
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setUuid(UUID.randomUUID().toString());
        vehicle1.setLicensePlateType(licensePlateTypeRepository.findById(1).orElseThrow());
        vehicle1.setVehicleType(vehicleTypeRepository.findById(1).orElseThrow());
        vehicle1.setUser(userRepository.findById(1).orElseThrow());
        vehicle1.setLicensePlateProvince(licensePlateProvinceRepository.findById(1).orElseThrow());
        vehicle1.setVehicleModel("Toyota");
        vehicle1.setVehicleMake("Honda");
        vehicle1.setNumberPlate("1234");
        vehicle1.setSites(List.of(siteRepository.findBySiteNameIgnoreCase("npic1").orElseThrow()));
        vehicle1.setCreatedAt(LocalDateTime.now());
        vehicle1.setColor("#000");
        vehicle1.setIsDeleted(false);
        vehicleRepository.save(vehicle1);
    }

}
