package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.BadRequestException;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

//@Service
@Slf4j
public class UserServiceImplJpa implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImplJpa(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        Person person = userMapper.userDtoToPerson(userDto);
        log.info("Mapped userDto: {}", userDto);

        Person personResponse = userRepository.save(person);
        log.info("Save person: {}", personResponse);

        userDto.setId(personResponse.getId());
        return userDto;
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto) {
        if (userDto.getId() < 1) throw new BadRequestException("Bad request");

        Person person = userRepository.findByIdForUpdate(userDto.getId())
                .orElseThrow(() -> new BadRequestException("id person not found"));
        log.info("Get person from bd: {}", person);

        person = userMapper.userDtoToPerson(userDto);
        log.info("Update person: {}", userDto);

        Person personResponse = userRepository.save(person);
        log.info("Save update person: {}", personResponse);

        userDto.setId(personResponse.getId());
        return userDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        Person person = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("id person not found"));

        UserDto userDto = userMapper.personToUserDto(person);
        log.info("Get id person: {}", id);
        return userDto;
    }

    @Override
    public void deleteUserById(Long id) {
        Person person = userRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("id person not found"));
        log.info("Get person from bd: {}", person);

        userRepository.deleteById(id);
        log.info("Delete person: {}", person);
    }
}
