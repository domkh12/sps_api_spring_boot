package edu.npic.sps.features.city;

import edu.npic.sps.features.city.dto.CityResponse;

import java.util.List;

public interface CityService {
    List<CityResponse> findAll();
}
