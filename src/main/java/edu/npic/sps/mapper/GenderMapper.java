package edu.npic.sps.mapper;

import edu.npic.sps.domain.Gender;
import edu.npic.sps.features.gender.dto.GenderRequest;
import edu.npic.sps.features.gender.dto.GenderResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface GenderMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromGenderRequest(GenderRequest genderRequest, @MappingTarget Gender gender);

    Gender fromGenderRequest(GenderRequest gender);

    GenderResponse toGenderResponse(Gender gender);

}
