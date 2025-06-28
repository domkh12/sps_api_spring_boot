package edu.npic.sps.features.signUpMethod;

import edu.npic.sps.features.signUpMethod.dto.SignUpMethodResponse;
import edu.npic.sps.features.signUpMethod.dto.SignupMethodRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sign-up-methods")
@RequiredArgsConstructor
@Tag(name = "Sign up method Management", description = "APIs for managing sign up methods")
public class SignUpMethodController {

    private final SignUpMethodService signUpMethodService;

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteByUuid(@PathVariable String uuid){
        signUpMethodService.delete(uuid);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.CREATED)
    SignUpMethodResponse updateByUuid(@PathVariable String uuid, @Valid @RequestBody SignupMethodRequest signupMethodRequest){
        return signUpMethodService.updateByUuid(uuid, signupMethodRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    SignUpMethodResponse create(@Valid @RequestBody SignupMethodRequest signupMethodRequest){
        return signUpMethodService.create(signupMethodRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<SignUpMethodResponse> findAll(){
        return signUpMethodService.findAll();
    }

}
