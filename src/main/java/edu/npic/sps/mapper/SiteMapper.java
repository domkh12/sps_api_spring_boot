package edu.npic.sps.mapper;

import edu.npic.sps.domain.Site;
import edu.npic.sps.features.site.dto.CreateSite;
import edu.npic.sps.features.site.dto.SiteResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SiteMapper {
    SiteResponse toSiteResponse(Site site);

    Site fromCreateSite(CreateSite createSite);
}
