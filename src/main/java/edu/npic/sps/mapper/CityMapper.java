package edu.npic.sps.mapper;

import edu.npic.sps.domain.City;
import edu.npic.sps.features.city.dto.CityRequest;
import edu.npic.sps.features.city.dto.CityResponse;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CityMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromCityRequest(CityRequest cityRequest, @MappingTarget City city);

    City fromCityResponse(CityRequest cityRequest);

    CityResponse toCityResponse(City city);
}
