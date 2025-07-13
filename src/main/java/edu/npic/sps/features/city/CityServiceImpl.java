package edu.npic.sps.features.city;

import edu.npic.sps.domain.City;
import edu.npic.sps.features.city.dto.CityRequest;
import edu.npic.sps.features.city.dto.CityResponse;
import edu.npic.sps.mapper.CityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CityServiceImpl implements CityService{
    private final CityRepository cityRepository;

    private final CityMapper cityMapper;

    @Override
    public CityResponse findByUuid(String uuid) {
        City city = cityRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found!")
        );
        return cityMapper.toCityResponse(city);
    }

    @Override
    public void delete(String uuid) {
        City city = cityRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found!")
        );
        cityRepository.deleteBySitesNullAndUuid(city.getUuid());
    }

    @Override
    public CityResponse updateByUuid(String uuid, CityRequest cityRequest) {

        if (cityRepository.existsByNameIgnoreCaseAndUuidNot(cityRequest.name().trim(), uuid)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "City name already exists!");
        }

        City city = cityRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "City not found!")
        );

        cityMapper.updateFromCityRequest(cityRequest, city);
        return cityMapper.toCityResponse(cityRepository.save(city));
    }

    @Override
    public CityResponse create(CityRequest cityRequest) {
        City city = cityMapper.fromCityResponse(cityRequest);
        city.setUuid(UUID.randomUUID().toString());
        city.setCreatedAt(LocalDateTime.now());
        City citySaved = cityRepository.save(city);
        return cityMapper.toCityResponse(citySaved);
    }

    @Override
    public List<CityResponse> findAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<City> cities = cityRepository.findAll(sort);
        return cities.stream().map(cityMapper::toCityResponse).toList();
    }
}
