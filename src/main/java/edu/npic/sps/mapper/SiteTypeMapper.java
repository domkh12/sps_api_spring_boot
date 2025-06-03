package edu.npic.sps.mapper;

import edu.npic.sps.domain.SiteType;
import edu.npic.sps.features.siteType.dto.SiteTypeRequest;
import edu.npic.sps.features.siteType.dto.SiteTypeResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.lang.annotation.Target;

@Mapper(componentModel = "spring")
public interface SiteTypeMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromSiteTypeRequest(SiteTypeRequest siteTypeRequest, @MappingTarget SiteType siteType);

    SiteType fromSiteTypeRequest(SiteTypeRequest siteTypeRequest);

    SiteTypeResponse toSiteTypeResponse(SiteType siteType);
}
