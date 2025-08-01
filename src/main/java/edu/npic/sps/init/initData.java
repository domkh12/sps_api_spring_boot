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

    @PostConstruct
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
        licensePlateType.setAlias("royalcambodianarmedforces");
        licensePlateType.setCreatedAt(LocalDateTime.now());
        licensePlateTypeRepository.save(licensePlateType);
    }

    private void initLicensePlateProvinces(){
        // Phnom Penh (Capital)
        LicensePlateProvince licensePlateProvince1 = new LicensePlateProvince();
        licensePlateProvince1.setUuid(UUID.randomUUID().toString());
        licensePlateProvince1.setProvinceNameEn("Phnom Penh");
        licensePlateProvince1.setProvinceNameKh("ភ្នំពេញ");
        licensePlateProvince1.setAlias("phnompenh");
        licensePlateProvince1.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince1);

        // Banteay Meanchey
        LicensePlateProvince licensePlateProvince2 = new LicensePlateProvince();
        licensePlateProvince2.setUuid(UUID.randomUUID().toString());
        licensePlateProvince2.setProvinceNameEn("Banteay Meanchey");
        licensePlateProvince2.setProvinceNameKh("បន្ទាយមានជ័យ");
        licensePlateProvince2.setAlias("banteaymeanchey");
        licensePlateProvince2.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince2);

        // Battambang
        LicensePlateProvince licensePlateProvince3 = new LicensePlateProvince();
        licensePlateProvince3.setUuid(UUID.randomUUID().toString());
        licensePlateProvince3.setProvinceNameEn("Battambang");
        licensePlateProvince3.setProvinceNameKh("បាត់ដំបង");
        licensePlateProvince3.setAlias("battambang");
        licensePlateProvince3.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince3);

        // Kampong Cham
        LicensePlateProvince licensePlateProvince4 = new LicensePlateProvince();
        licensePlateProvince4.setUuid(UUID.randomUUID().toString());
        licensePlateProvince4.setProvinceNameEn("Kampong Cham");
        licensePlateProvince4.setProvinceNameKh("កំពង់ចាម");
        licensePlateProvince4.setAlias("kampongcham");
        licensePlateProvince4.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince4);

        // Kampong Chhnang
        LicensePlateProvince licensePlateProvince5 = new LicensePlateProvince();
        licensePlateProvince5.setUuid(UUID.randomUUID().toString());
        licensePlateProvince5.setProvinceNameEn("Kampong Chhnang");
        licensePlateProvince5.setProvinceNameKh("កំពង់ឆ្នាំង");
        licensePlateProvince5.setAlias("kampongchhnang");
        licensePlateProvince5.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince5);

        // Kampong Speu
        LicensePlateProvince licensePlateProvince6 = new LicensePlateProvince();
        licensePlateProvince6.setUuid(UUID.randomUUID().toString());
        licensePlateProvince6.setProvinceNameEn("Kampong Speu");
        licensePlateProvince6.setProvinceNameKh("កំពង់ស្ពឺ");
        licensePlateProvince6.setAlias("kampongspeu");
        licensePlateProvince6.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince6);

        // Kampong Thom
        LicensePlateProvince licensePlateProvince7 = new LicensePlateProvince();
        licensePlateProvince7.setUuid(UUID.randomUUID().toString());
        licensePlateProvince7.setProvinceNameEn("Kampong Thom");
        licensePlateProvince7.setProvinceNameKh("កំពង់ធំ");
        licensePlateProvince7.setAlias("kampongthom");
        licensePlateProvince7.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince7);

        // Kampot
        LicensePlateProvince licensePlateProvince8 = new LicensePlateProvince();
        licensePlateProvince8.setUuid(UUID.randomUUID().toString());
        licensePlateProvince8.setProvinceNameEn("Kampot");
        licensePlateProvince8.setProvinceNameKh("កំពត");
        licensePlateProvince8.setAlias("kampot");
        licensePlateProvince8.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince8);

        // Kandal
        LicensePlateProvince licensePlateProvince9 = new LicensePlateProvince();
        licensePlateProvince9.setUuid(UUID.randomUUID().toString());
        licensePlateProvince9.setProvinceNameEn("Kandal");
        licensePlateProvince9.setProvinceNameKh("កណ្ដាល");
        licensePlateProvince9.setAlias("kandal");
        licensePlateProvince9.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince9);

        // Koh Kong
        LicensePlateProvince licensePlateProvince10 = new LicensePlateProvince();
        licensePlateProvince10.setUuid(UUID.randomUUID().toString());
        licensePlateProvince10.setProvinceNameEn("Koh Kong");
        licensePlateProvince10.setProvinceNameKh("កោះកុង");
        licensePlateProvince10.setAlias("kohkong");
        licensePlateProvince10.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince10);

        // Kratie
        LicensePlateProvince licensePlateProvince11 = new LicensePlateProvince();
        licensePlateProvince11.setUuid(UUID.randomUUID().toString());
        licensePlateProvince11.setProvinceNameEn("Kratie");
        licensePlateProvince11.setProvinceNameKh("ក្រចេះ");
        licensePlateProvince11.setAlias("kratie");
        licensePlateProvince11.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince11);

        // Mondulkiri
        LicensePlateProvince licensePlateProvince12 = new LicensePlateProvince();
        licensePlateProvince12.setUuid(UUID.randomUUID().toString());
        licensePlateProvince12.setProvinceNameEn("Mondulkiri");
        licensePlateProvince12.setProvinceNameKh("មណ្ឌលគិរី");
        licensePlateProvince12.setAlias("mondulkiri");
        licensePlateProvince12.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince12);

        // Oddar Meanchey
        LicensePlateProvince licensePlateProvince13 = new LicensePlateProvince();
        licensePlateProvince13.setUuid(UUID.randomUUID().toString());
        licensePlateProvince13.setProvinceNameEn("Oddar Meanchey");
        licensePlateProvince13.setProvinceNameKh("ឧត្ដរមានជ័យ");
        licensePlateProvince13.setAlias("oddarmeanchey");
        licensePlateProvince13.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince13);

        // Pailin
        LicensePlateProvince licensePlateProvince14 = new LicensePlateProvince();
        licensePlateProvince14.setUuid(UUID.randomUUID().toString());
        licensePlateProvince14.setProvinceNameEn("Pailin");
        licensePlateProvince14.setProvinceNameKh("ប៉ៃលិន");
        licensePlateProvince14.setAlias("pailin");
        licensePlateProvince14.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince14);

        // Preah Vihear
        LicensePlateProvince licensePlateProvince15 = new LicensePlateProvince();
        licensePlateProvince15.setUuid(UUID.randomUUID().toString());
        licensePlateProvince15.setProvinceNameEn("Preah Vihear");
        licensePlateProvince15.setProvinceNameKh("ព្រះវិហារ");
        licensePlateProvince15.setAlias("preahvihear");
        licensePlateProvince15.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince15);

        // Prey Veng
        LicensePlateProvince licensePlateProvince16 = new LicensePlateProvince();
        licensePlateProvince16.setUuid(UUID.randomUUID().toString());
        licensePlateProvince16.setProvinceNameEn("Prey Veng");
        licensePlateProvince16.setProvinceNameKh("ព្រៃវែង");
        licensePlateProvince16.setAlias("preyveng");
        licensePlateProvince16.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince16);

        // Pursat
        LicensePlateProvince licensePlateProvince17 = new LicensePlateProvince();
        licensePlateProvince17.setUuid(UUID.randomUUID().toString());
        licensePlateProvince17.setProvinceNameEn("Pursat");
        licensePlateProvince17.setProvinceNameKh("ពោធិ៍សាត់");
        licensePlateProvince17.setAlias("pursat");
        licensePlateProvince17.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince17);

        // Ratanakiri
        LicensePlateProvince licensePlateProvince18 = new LicensePlateProvince();
        licensePlateProvince18.setUuid(UUID.randomUUID().toString());
        licensePlateProvince18.setProvinceNameEn("Ratanakiri");
        licensePlateProvince18.setProvinceNameKh("រតនគិរី");
        licensePlateProvince18.setAlias("ratanakiri");
        licensePlateProvince18.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince18);

        // Siem Reap
        LicensePlateProvince licensePlateProvince19 = new LicensePlateProvince();
        licensePlateProvince19.setUuid(UUID.randomUUID().toString());
        licensePlateProvince19.setProvinceNameEn("Siem Reap");
        licensePlateProvince19.setProvinceNameKh("សៀមរាប");
        licensePlateProvince19.setAlias("siemreap");
        licensePlateProvince19.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince19);

        // Preah Sihanouk (Sihanoukville)
        LicensePlateProvince licensePlateProvince20 = new LicensePlateProvince();
        licensePlateProvince20.setUuid(UUID.randomUUID().toString());
        licensePlateProvince20.setProvinceNameEn("Preah Sihanouk");
        licensePlateProvince20.setProvinceNameKh("ព្រះសីហនុ");
        licensePlateProvince20.setAlias("preahsihanouk");
        licensePlateProvince20.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince20);

        // Stung Treng
        LicensePlateProvince licensePlateProvince21 = new LicensePlateProvince();
        licensePlateProvince21.setUuid(UUID.randomUUID().toString());
        licensePlateProvince21.setProvinceNameEn("Stung Treng");
        licensePlateProvince21.setProvinceNameKh("ស្ទឹងត្រែង");
        licensePlateProvince21.setAlias("stungtreng");
        licensePlateProvince21.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince21);

        // Svay Rieng
        LicensePlateProvince licensePlateProvince22 = new LicensePlateProvince();
        licensePlateProvince22.setUuid(UUID.randomUUID().toString());
        licensePlateProvince22.setProvinceNameEn("Svay Rieng");
        licensePlateProvince22.setProvinceNameKh("ស្វាយរៀង");
        licensePlateProvince22.setAlias("svayrieng");
        licensePlateProvince22.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince22);

        // Takeo
        LicensePlateProvince licensePlateProvince23 = new LicensePlateProvince();
        licensePlateProvince23.setUuid(UUID.randomUUID().toString());
        licensePlateProvince23.setProvinceNameEn("Takeo");
        licensePlateProvince23.setProvinceNameKh("តាកែវ");
        licensePlateProvince23.setAlias("takeo");
        licensePlateProvince23.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince23);

        // Tbong Khmum
        LicensePlateProvince licensePlateProvince24 = new LicensePlateProvince();
        licensePlateProvince24.setUuid(UUID.randomUUID().toString());
        licensePlateProvince24.setProvinceNameEn("Tbong Khmum");
        licensePlateProvince24.setProvinceNameKh("ត្បូងឃ្មុំ");
        licensePlateProvince24.setAlias("tbongkhmum");
        licensePlateProvince24.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince24);

        // Kep
        LicensePlateProvince licensePlateProvince25 = new LicensePlateProvince();
        licensePlateProvince25.setUuid(UUID.randomUUID().toString());
        licensePlateProvince25.setProvinceNameEn("Kep");
        licensePlateProvince25.setProvinceNameKh("កែប");
        licensePlateProvince25.setAlias("kep");
        licensePlateProvince25.setCreatedAt(LocalDateTime.now());
        licensePlateProvinceRepository.save(licensePlateProvince25);
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
        male.setCreatedAt(LocalDateTime.now());
        Gender female = new Gender();
        female.setUuid(UUID.randomUUID().toString());
        female.setCreatedAt(LocalDateTime.now());
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
        roles.add(roleRepository.findById(2).orElseThrow(() -> new RuntimeException("Role 1 not found")));
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
