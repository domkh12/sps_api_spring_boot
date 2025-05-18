package edu.npic.sps.mapper;

import edu.npic.sps.domain.Gender;
import edu.npic.sps.features.gender.dto.GenderResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GenderMapper {

    GenderResponse toGenderResponse(Gender gender);

}
