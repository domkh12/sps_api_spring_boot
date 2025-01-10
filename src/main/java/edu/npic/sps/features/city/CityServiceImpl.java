package edu.npic.sps.features.city;

import edu.npic.sps.domain.City;
import edu.npic.sps.features.city.dto.CityResponse;
import edu.npic.sps.mapper.CityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityServiceImpl implements CityService{

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Override
    public List<CityResponse> findAll() {
        List<City> cities = cityRepository.findAll();
        return cities.stream().map(city -> cityMapper.toCityResponse(city)).toList();
    }
}
