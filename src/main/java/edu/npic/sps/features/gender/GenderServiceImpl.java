package edu.npic.sps.features.gender;

import edu.npic.sps.domain.Gender;
import edu.npic.sps.features.gender.dto.GenderResponse;
import edu.npic.sps.mapper.GenderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenderServiceImpl implements GenderService{

    private final GenderRepository genreRepository;
    private final GenderMapper genderMapper;

    @Override
    public List<GenderResponse> findAll() {
        List<Gender> genders = genreRepository.findAll();
        return genders.stream().map(gender -> genderMapper.toGenderResponse(gender)).toList();
    }
}
