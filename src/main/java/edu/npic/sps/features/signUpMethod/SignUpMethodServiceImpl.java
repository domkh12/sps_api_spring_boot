package edu.npic.sps.features.signUpMethod;

import edu.npic.sps.domain.SignUpMethod;
import edu.npic.sps.features.signUpMethod.dto.SignUpMethodResponse;
import edu.npic.sps.mapper.SignUpMethodMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SignUpMethodServiceImpl implements SignUpMethodService{

    private final SignUpMethodRepository signUpMethodRepository;
    private final SignUpMethodMapper signUpMethodMapper;

    @Override
    public List<SignUpMethodResponse> findAll() {
        List<SignUpMethod> signUpMethodResponses = signUpMethodRepository.findAll();
        return signUpMethodResponses.stream()
                .map(signUpMethod -> signUpMethodMapper.toSignUpMethodResponse(signUpMethod)).toList();
    }
}
