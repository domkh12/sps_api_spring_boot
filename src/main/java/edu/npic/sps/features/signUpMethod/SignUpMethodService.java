package edu.npic.sps.features.signUpMethod;

import edu.npic.sps.features.signUpMethod.dto.SignUpMethodResponse;

import java.util.List;

public interface SignUpMethodService {
    List<SignUpMethodResponse> findAll();
}
