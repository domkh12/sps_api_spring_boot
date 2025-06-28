package edu.npic.sps.features.signUpMethod;

import edu.npic.sps.domain.SignUpMethod;
import edu.npic.sps.features.signUpMethod.dto.SignUpMethodResponse;
import edu.npic.sps.features.signUpMethod.dto.SignupMethodRequest;
import edu.npic.sps.mapper.SignUpMethodMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SignUpMethodServiceImpl implements SignUpMethodService{

    private final SignUpMethodRepository signUpMethodRepository;
    private final SignUpMethodMapper signUpMethodMapper;

    @Override
    public void delete(String uuid) {
        SignUpMethod signUpMethod = signUpMethodRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sign up method not found!")
        );
        signUpMethodRepository.deleteByUuid(signUpMethod.getUuid());
    }

    @Override
    public SignUpMethodResponse updateByUuid(String uuid, SignupMethodRequest signupMethodRequest) {
        SignUpMethod signUpMethod = signUpMethodRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sign up method not found!")
        );

        signUpMethodMapper.updateFromSignupMethodRequest(signupMethodRequest, signUpMethod);

        return signUpMethodMapper.toSignUpMethodResponse(signUpMethodRepository.save(signUpMethod));
    }

    @Override
    public SignUpMethodResponse create(SignupMethodRequest signupMethodRequest) {

        if (signUpMethodRepository.existsByNameIgnoreCase(signupMethodRequest.name().trim())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Sign up method name already exists!");
        }

        SignUpMethod signUpMethod = signUpMethodMapper.fromSignupMethodRequest(signupMethodRequest);
        signUpMethod.setUuid(UUID.randomUUID().toString());
        SignUpMethod signUpMethodSaved = signUpMethodRepository.save(signUpMethod);

        return signUpMethodMapper.toSignUpMethodResponse(signUpMethodSaved);
    }

    @Override
    public List<SignUpMethodResponse> findAll() {
        List<SignUpMethod> signUpMethodResponses = signUpMethodRepository.findAll();
        return signUpMethodResponses.stream()
                .map(signUpMethod -> signUpMethodMapper.toSignUpMethodResponse(signUpMethod)).toList();
    }
}
