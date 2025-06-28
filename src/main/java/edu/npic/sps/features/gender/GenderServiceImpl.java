package edu.npic.sps.features.gender;

import edu.npic.sps.domain.Gender;
import edu.npic.sps.features.gender.dto.GenderRequest;
import edu.npic.sps.features.gender.dto.GenderResponse;
import edu.npic.sps.mapper.GenderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GenderServiceImpl implements GenderService{

    private final GenderRepository genderRepository;
    private final GenderMapper genderMapper;

    @Override
    public void delete(String uuid) {
        Gender gender = genderRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gender not found!")
        );
        genderRepository.delete(gender);
    }

    @Override
    public GenderResponse updateByUuid(String uuid, GenderRequest genderRequest) {
        Gender gender = genderRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gender not found!")
        );
        genderMapper.updateFromGenderRequest(genderRequest, gender);

        return genderMapper.toGenderResponse(genderRepository.save(gender));
    }

    @Override
    public GenderResponse create(GenderRequest genderRequest) {
        Gender gender = genderMapper.fromGenderRequest(genderRequest);
        gender.setUuid(UUID.randomUUID().toString());
        Gender genderSaved = genderRepository.save(gender);
        return genderMapper.toGenderResponse(genderSaved);
    }

    @Override
    public List<GenderResponse> findAll() {
        List<Gender> genders = genderRepository.findAll();
        return genders.stream().map(genderMapper::toGenderResponse).toList();
    }
}
