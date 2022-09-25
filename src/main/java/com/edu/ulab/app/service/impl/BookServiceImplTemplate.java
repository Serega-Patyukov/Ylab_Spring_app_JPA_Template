package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.exception.BadRequestException;
import com.edu.ulab.app.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

@Slf4j
@Service
public class BookServiceImplTemplate implements BookService {

    private final JdbcTemplate jdbcTemplate;
    private final JdbcOperations jdbcOperations;
    private final UserServiceImplTemplate userServiceImplTemplate;

    public BookServiceImplTemplate(JdbcTemplate jdbcTemplate, JdbcOperations jdbcOperations, UserServiceImplTemplate userServiceImplTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcOperations = jdbcOperations;
        this.userServiceImplTemplate = userServiceImplTemplate;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {

        userServiceImplTemplate.getUserById(bookDto.getUserId());   // Проверим, есть ли пользователь с таким id

        final String INSERT_SQL = "INSERT INTO BOOK(TITLE, AUTHOR, PAGE_COUNT, USER_ID) VALUES (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                        ps.setString(1, bookDto.getTitle());
                        ps.setString(2, bookDto.getAuthor());
                        ps.setLong(3, bookDto.getPageCount());
                        ps.setLong(4, bookDto.getUserId());
                        return ps;
                    }
                },
                keyHolder);

        bookDto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        log.info("Save book : {}", bookDto);

        return bookDto;
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {

        userServiceImplTemplate.getUserById(bookDto.getUserId());   // Проверим, есть ли пользователь с таким id

        final String UPDATE_SQL = "UPDATE BOOK SET USER_ID = ?, TITLE = ?, AUTHOR = ?, PAGE_COUNT = ? WHERE ID = ?";
        int amountUpdateBook = jdbcTemplate.update(UPDATE_SQL,
                bookDto.getUserId(),
                bookDto.getTitle(),
                bookDto.getAuthor(),
                bookDto.getPageCount(),
                bookDto.getId());

        if (amountUpdateBook == 0) throw  new BadRequestException("id book not found");

        log.info("Update book : {}", bookDto);

        return bookDto;
    }

    @Override
    public Iterable<BookDto> getBookById(Long id) {
        final String SELECT_SQL = "SELECT USER_ID, ID, TITLE, AUTHOR, PAGE_COUNT FROM BOOK WHERE USER_ID = ?";
        return jdbcOperations.query(
                SELECT_SQL,
                (rs, rowNum) -> {
                    BookDto bookDto = new BookDto();
                    bookDto.setUserId(rs.getLong("USER_ID"));
                    bookDto.setId(rs.getLong("ID"));
                    bookDto.setTitle(rs.getString("TITLE"));
                    bookDto.setAuthor(rs.getString("AUTHOR"));
                    bookDto.setPageCount(rs.getLong("PAGE_COUNT"));
                    log.info("Get id book: {}", id);
                    return bookDto;
                },
                id);
    }

    @Override
    public void deleteBookById(Long id) {
        final String DELETE_SQL = "DELETE FROM BOOK WHERE USER_ID = ?";
        int amountDeleteBook = jdbcTemplate.update(DELETE_SQL, id);
        log.info("Amount delete book on userId = " + id + ": {}", amountDeleteBook);
    }
}
