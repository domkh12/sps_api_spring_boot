package edu.npic.sps.mapper;

import edu.npic.sps.domain.User;
import edu.npic.sps.features.user.dto.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

   List<FullNameResponse> toFullNameResponse(List<User> users);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromUpdateStatusRequest(IsOnlineRequest isOnlineRequest, @MappingTarget User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void fromUpdateUserRequest(UpdateUserRequest updateUserRequest, @MappingTarget User user);

    User fromCreateUserRegister(CreateUserRegister createUserRegister);

    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User fromCreateUser(CreateUser createUser);

    UserDetailResponse toUserDetailResponse(User user);

}
