package edu.npic.sps.features.city;

import edu.npic.sps.domain.City;
import edu.npic.sps.features.city.dto.CityResponse;
import edu.npic.sps.mapper.CityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CityServiceImpl implements CityService{

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Override
    public List<CityResponse> findAll() {
        List<City> cities = cityRepository.findAll();
        return cities.stream().map(cityMapper::toCityResponse).toList();
    }
}
