package edu.npic.sps.features.gender;

import edu.npic.sps.features.gender.dto.GenderRequest;
import edu.npic.sps.features.gender.dto.GenderResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface GenderService {
    GenderResponse findByUuid(String uuid);

    void delete(String uuid);

    GenderResponse updateByUuid(String uuid, GenderRequest genderRequest);

    GenderResponse create(@Valid GenderRequest genderRequest);

    List<GenderResponse> findAll();
}
