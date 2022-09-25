package com.edu.ulab.app.mapper;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.entityStorage.UserEntity;
import com.edu.ulab.app.entity.entityJpaTemplate.Person;
import com.edu.ulab.app.web.request.UserRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto userRequestToUserDto(UserRequest userRequest);

    UserRequest userDtoToUserRequest(UserDto userDto);

    Person userDtoToPerson(UserDto userDto);

    UserDto personToUserDto(Person person);

    UserEntity userDtoToUserEntity(UserDto userDto);

    UserDto userEntityToUserDto(UserEntity userEntity);

    UserEntity userEntityToUserEntity(UserEntity userEntity);
}
