package edu.npic.sps.mapper;

import edu.npic.sps.domain.Company;
import edu.npic.sps.domain.Site;
import edu.npic.sps.features.company.dto.CompanyNameResponse;
import edu.npic.sps.features.company.dto.CompanyResponse;
import edu.npic.sps.features.company.dto.CompanyRequest;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromCompanyRequest(CompanyRequest companyRequest, @MappingTarget Company company);

    Company fromCreateCompany(CompanyRequest companyRequest);

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
