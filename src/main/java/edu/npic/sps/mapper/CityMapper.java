package edu.npic.sps.mapper;

import edu.npic.sps.domain.City;
import edu.npic.sps.features.city.dto.CityResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CityMapper {
    CityResponse toCityResponse(City city);
}
