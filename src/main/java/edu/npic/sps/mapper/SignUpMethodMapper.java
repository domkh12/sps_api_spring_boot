package edu.npic.sps.mapper;

import edu.npic.sps.domain.SignUpMethod;
import edu.npic.sps.features.signUpMethod.dto.SignUpMethodResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SignUpMethodMapper {

    SignUpMethodResponse toSignUpMethodResponse(SignUpMethod signUpMethod);

}
