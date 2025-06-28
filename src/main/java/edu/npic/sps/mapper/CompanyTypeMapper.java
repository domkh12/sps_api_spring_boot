package edu.npic.sps.mapper;

import edu.npic.sps.domain.CompanyType;
import edu.npic.sps.features.companyType.dto.CompanyTypeRequest;
import edu.npic.sps.features.companyType.dto.CompanyTypeResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CompanyTypeMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromCompanyTypeRequest(CompanyTypeRequest companyTypeRequest, @MappingTarget CompanyType companyType);

    CompanyType fromCompanyTypeRequest(CompanyTypeRequest companyTypeRequest);

    CompanyTypeResponse toCompanyTypeResponse(CompanyType companyType);
}
