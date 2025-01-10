package edu.npic.sps.features.gender;

import edu.npic.sps.features.gender.dto.GenderResponse;

import java.util.List;

public interface GenderService {
    List<GenderResponse> findAll();
}
