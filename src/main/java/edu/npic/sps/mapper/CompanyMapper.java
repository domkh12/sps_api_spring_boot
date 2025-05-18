package edu.npic.sps.mapper;

import edu.npic.sps.domain.Company;
import edu.npic.sps.domain.Site;
import edu.npic.sps.features.company.dto.CompanyNameResponse;
import edu.npic.sps.features.company.dto.CompanyResponse;
import edu.npic.sps.features.company.dto.CreateCompany;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    @Named("mapSiteNamesToSites")
    default List<Site> mapSiteNamesToSites(List<String> siteNames) {
        return siteNames.stream()
                .map(
                        siteName -> {
                            Site site = new Site();
                            site.setSiteName(siteName);
                            return site;
                        })
                .toList();
    }
    CompanyResponse toCompanyResponse(Company company);

    CompanyNameResponse toCompanyNameResponse(Company company);

}
