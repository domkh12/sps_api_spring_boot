package edu.npic.sps.features.signUpMethod;

import edu.npic.sps.features.signUpMethod.dto.SignUpMethodResponse;
import edu.npic.sps.features.signUpMethod.dto.SignupMethodRequest;
import jakarta.validation.Valid;

import java.util.List;

public interface SignUpMethodService {
    void delete(String uuid);

    SignUpMethodResponse updateByUuid(String uuid, SignupMethodRequest signupMethodRequest);

    SignUpMethodResponse create(@Valid SignupMethodRequest signupMethodRequest);

    List<SignUpMethodResponse> findAll();
}
