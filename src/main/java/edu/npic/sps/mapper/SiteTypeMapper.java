package edu.npic.sps.mapper;

import edu.npic.sps.domain.SiteType;
import edu.npic.sps.features.siteType.dto.SiteTypeResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SiteTypeMapper {

    SiteTypeResponse toSiteTypeResponse(SiteType siteType);
}
