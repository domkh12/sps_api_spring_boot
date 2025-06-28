package edu.npic.sps.features.city;

import edu.npic.sps.features.city.dto.CityRequest;
import edu.npic.sps.features.city.dto.CityResponse;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface CityService {

    void delete(String uuid);

    CityResponse create(@Valid CityRequest cityRequest);

    List<CityResponse> findAll();

    CityResponse updateByUuid(String uuid, @Valid CityRequest cityRequest);
}
