package edu.npic.sps.features.gender;

import edu.npic.sps.domain.Gender;
import edu.npic.sps.features.gender.dto.GenderRequest;
import edu.npic.sps.features.gender.dto.GenderResponse;
import edu.npic.sps.mapper.GenderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GenderServiceImpl implements GenderService{

    private final GenderRepository genderRepository;
    private final GenderMapper genderMapper;

    @Override
    public GenderResponse findByUuid(String uuid) {
        Gender gender = genderRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gender not found!")
        );
        return genderMapper.toGenderResponse(gender);
    }

    @Override
    public void delete(String uuid) {
        Gender gender = genderRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gender not found!")
        );
        genderRepository.deleteByUuid(gender.getUuid());
    }

    @Override
    public GenderResponse updateByUuid(String uuid, GenderRequest genderRequest) {

        if (genderRepository.existsByGenderIgnoreCaseAndUuidNot(genderRequest.gender(), uuid)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Gender already exists!");
        }

        Gender gender = genderRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Gender not found!")
        );
        genderMapper.updateFromGenderRequest(genderRequest, gender);

        return genderMapper.toGenderResponse(genderRepository.save(gender));
    }

    @Override
    public GenderResponse create(GenderRequest genderRequest) {
        if(genderRepository.existsByGenderIgnoreCase(genderRequest.gender())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Gender already exists!");
        }
        Gender gender = genderMapper.fromGenderRequest(genderRequest);
        gender.setUuid(UUID.randomUUID().toString());
        gender.setCreatedAt(LocalDateTime.now());
        Gender genderSaved = genderRepository.save(gender);
        return genderMapper.toGenderResponse(genderSaved);
    }

    @Override
    public List<GenderResponse> findAll() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        List<Gender> genders = genderRepository.findAll(sort);
        return genders.stream().map(genderMapper::toGenderResponse).toList();
    }
}
