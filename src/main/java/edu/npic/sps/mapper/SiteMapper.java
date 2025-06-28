package edu.npic.sps.mapper;

import edu.npic.sps.domain.Site;
import edu.npic.sps.features.site.dto.CreateSite;
import edu.npic.sps.features.site.dto.SiteRequest;
import edu.npic.sps.features.site.dto.SiteResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SiteMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromSiteRequest(SiteRequest siteRequest, @MappingTarget Site site);

    SiteResponse toSiteResponse(Site site);

    Site fromCreateSite(CreateSite createSite);
}
