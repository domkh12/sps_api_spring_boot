package edu.npic.sps.mapper;

import edu.npic.sps.domain.SignUpMethod;
import edu.npic.sps.features.signUpMethod.dto.SignUpMethodResponse;
import edu.npic.sps.features.signUpMethod.dto.SignupMethodRequest;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface SignUpMethodMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromSignupMethodRequest(SignupMethodRequest signupMethodRequest, @MappingTarget SignUpMethod signUpMethod);

    SignUpMethod fromSignupMethodRequest(SignupMethodRequest signupMethodRequest);

    SignUpMethodResponse toSignUpMethodResponse(SignUpMethod signUpMethod);

}
