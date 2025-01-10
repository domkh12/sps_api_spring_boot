package edu.npic.sps.features.site;

import edu.npic.sps.domain.*;
import edu.npic.sps.features.city.CityRepository;
import edu.npic.sps.features.company.CompanyRepository;
import edu.npic.sps.features.site.dto.CreateSite;
import edu.npic.sps.features.site.dto.SiteResponse;
import edu.npic.sps.features.siteType.SiteTypeRepository;
import edu.npic.sps.mapper.SiteMapper;
import edu.npic.sps.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        site.setCreatedAt(LocalDateTime.now());
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
    public Page<SiteResponse> findAll(int pageNo, int pageSize, String searchTerm) {

        if (pageNo < 1 || pageSize < 1) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page number or page size must be greater than zero"
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Site> sites;
        if (!searchTerm.isEmpty()){
            sites = siteRepository.searchSites(searchTerm, pageRequest);
        }else {
            sites = siteRepository.findAll(pageRequest);
        }

        return sites.map(siteMapper::toSiteResponse);
    }
}
