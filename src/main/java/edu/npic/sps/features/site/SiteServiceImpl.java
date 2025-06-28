package edu.npic.sps.features.site;

import edu.npic.sps.domain.*;
import edu.npic.sps.features.city.CityRepository;
import edu.npic.sps.features.company.CompanyRepository;
import edu.npic.sps.features.parkingSpace.ParkingSpaceRepository;
import edu.npic.sps.features.site.dto.CreateSite;
import edu.npic.sps.features.site.dto.SiteRequest;
import edu.npic.sps.features.site.dto.SiteResponse;
import edu.npic.sps.features.siteType.SiteTypeRepository;
import edu.npic.sps.features.user.UserRepository;
import edu.npic.sps.features.vehicle.VehicleRepository;
import edu.npic.sps.mapper.SiteMapper;
import edu.npic.sps.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
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
public class SiteServiceImpl implements SiteService{

    private final SiteRepository siteRepository;
    private final SiteMapper siteMapper;
    private final AuthUtil authUtil;
    private final SiteTypeRepository siteTypeRepository;
    private final CityRepository cityRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingSpaceRepository parkingSpaceRepository;

    @Override
    public SiteResponse findByUuid(String uuid) {

        Site site = siteRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Site not found!")
        );

        return siteMapper.toSiteResponse(site);
    }

    @Override
    public Page<SiteResponse> filter(int pageNo, int pageSize, String keywords, String cityId, String siteTypeId, String companyId) {

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

        List<String> cityIds = null;
        if(cityId!=null && !cityId.isEmpty()){
            cityIds = Arrays.stream(cityId.split(","))
                    .map(String::trim)
                    .toList();
        }

        List<String> siteTypeIds = null;
        if(siteTypeId!=null && !siteTypeId.isEmpty()){
            siteTypeIds = Arrays.stream(siteTypeId.split(","))
                    .map(String::trim)
                    .toList();
        }

        List<String> companyIds = null;
        if(companyId!=null && !companyId.isEmpty()){
            companyIds = Arrays.stream(companyId.split(","))
                    .map(String::trim)
                    .toList();
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Site> sites = siteRepository.filterSite(keywords, cityIds, siteTypeIds, companyIds, pageable);

        return sites.map(siteMapper::toSiteResponse);
    }

    @Override
    public void delete(String uuid) {

        Site site = siteRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found!")
        );

        Company company = site.getCompany();
        siteRepository.deleteByUuid(site.getUuid());
        company.setSiteQty(company.getSiteQty() - 1);
        companyRepository.save(company);
    }

    @Override
    public SiteResponse createSite(CreateSite createSite) {

        if (siteRepository.existsBySiteName(createSite.siteName())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Branch name already exist!"
            );
        }

        Site site = siteMapper.fromCreateSite(createSite);
        SiteType siteType = siteTypeRepository.findByUuid(createSite.siteTypeUuid()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Site type not found!")
        );
        City city = cityRepository.findByUuid(createSite.cityUuid()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found!")
        );
        Company company = companyRepository.findByUuid(createSite.companyUuid()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found!")
        );
        site.setUuid(UUID.randomUUID().toString());
        site.setCompany(company);
        site.setCity(city);
        site.setSiteType(siteType);
        site.setParkingSpacesQty(0);
        site.setCreatedAt(LocalDateTime.now());
        siteRepository.save(site);
        company.setSiteQty(company.getSiteQty() + 1);
        companyRepository.save(company);
        return siteMapper.toSiteResponse(site);
    }

    @Override
    public SiteResponse update(String uuid, SiteRequest siteRequest) {

        Site site = siteRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Site not found!")
        );

        if (!site.getSiteName().equals(siteRequest.siteName()) && siteRepository.existsBySiteNameIgnoreCase(siteRequest.siteName())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Branch name already exist!"
            );
        }

        if (siteRequest.siteTypeUuid() != null){
            SiteType siteType = siteTypeRepository.findByUuid(siteRequest.siteTypeUuid()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Site type not found!")
            );
            site.setSiteType(siteType);
        }

        if (siteRequest.companyUuid() != null){
            Company company = companyRepository.findByUuid(siteRequest.companyUuid()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found!")
            );
            site.setCompany(company);
        }

        if (siteRequest.cityUuid() != null){
            City city = cityRepository.findByUuid(siteRequest.cityUuid()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found!")
            );
            site.setCity(city);
        }

        siteMapper.fromSiteRequest(siteRequest, site);
        siteRepository.save(site);

        return siteMapper.toSiteResponse(site);
    }

    @Override
    public List<SiteResponse> findAllByUserRole() {
        User user = authUtil.loggedUser();
        boolean isAdmin = authUtil.isAdminLoggedUser();
        boolean isManager = authUtil.isManagerLoggedUser();
        List<Site> sites = new ArrayList<>();
        if(isManager) {
            sites = siteRepository.findAll();
        }else if(isAdmin){
            sites = user.getSites().stream().map(
                    site -> siteRepository.findByUuid(site.getUuid()).orElseThrow()
            ).toList();
        }

        return sites.stream().map(site -> siteMapper.toSiteResponse(site)).toList();
    }

    @Override
    public Page<SiteResponse> findAll(int pageNo, int pageSize ) {

        boolean isAdmin = authUtil.isAdminLoggedUser();
        boolean isManager = authUtil.isManagerLoggedUser();
        String userUuid = authUtil.loggedUserUuid();

        if (pageNo < 1 || pageSize < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page number or page size must be greater than zero"
            );
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Site> sites = Page.empty();
        if (isManager){
            sites = siteRepository.findAll(pageRequest);
        }else if(isAdmin){
            sites = siteRepository.findByUsers_Uuid(userUuid, pageRequest);
        }


        return sites.map(siteMapper::toSiteResponse);
    }
}
