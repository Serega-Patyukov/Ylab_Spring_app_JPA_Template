package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.exception.BadRequestException;
import com.edu.ulab.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

//@Service
@Slf4j
public class UserServiceImplTemplate implements UserService {
    private final JdbcTemplate jdbcTemplate;

    public UserServiceImplTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDto createUser(UserDto userDto) {

        final String INSERT_SQL = "INSERT INTO PERSON(FULL_NAME, TITLE, AGE) VALUES (?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                    ps.setString(1, userDto.getFullName());
                    ps.setString(2, userDto.getTitle());
                    ps.setLong(3, userDto.getAge());
                    return ps;
                }, keyHolder);

        userDto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        log.info("Save person: {}", userDto);

        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto) {

        getUserById(userDto.getId());   // Проверим, есть ли пользователь с таким id

        final String UPDATE_SQL = "UPDATE PERSON SET FULL_NAME = ?, TITLE = ?, AGE = ? WHERE ID = ?";
        jdbcTemplate.update(UPDATE_SQL,
                userDto.getFullName(),
                userDto.getTitle(),
                userDto.getAge(),
                userDto.getId());

        log.info("Update person: {}", userDto);

        return userDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        final String SELECT_SQL = "SELECT ID, FULL_NAME, TITLE, AGE FROM PERSON WHERE ID = ?";
        try {
            UserDto userDto = jdbcTemplate.queryForObject(
                    SELECT_SQL,
                    (rs, rowNum) -> {
                        UserDto dto = new UserDto();
                        dto.setId((long) rs.getInt("ID"));
                        dto.setFullName(rs.getString("FULL_NAME"));
                        dto.setTitle(rs.getString("TITLE"));
                        dto.setAge(rs.getInt("AGE"));
                        return dto;
                    },
                    id);
            log.info("Get id person: {}", id);
            return userDto;
        } catch (IncorrectResultSizeDataAccessException exc) {
            throw new BadRequestException("id person not found");
        }
    }

    @Override
    public void deleteUserById(Long id) {
        final String DELETE_SQL = "DELETE FROM PERSON WHERE ID = ?";
        int amountDeletePerson = jdbcTemplate.update(DELETE_SQL, id);
        log.info("Amount delete person on id = " + id + ": {}", amountDeletePerson);
    }
}
