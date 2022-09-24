package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.service.BookService;
import lombok.extern.slf4j.Slf4j;
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

    public BookServiceImplTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {

        //todo тут нужно проверить пользователя, есть он в бд или нет

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

        //todo тут нужно проверить пользователя, есть он в бд или нет
        // и проверить книгу

        final String UPDATE_SQL = "UPDATE BOOK SET USER_ID = ?, TITLE = ?, AUTHOR = ?, PAGE_COUNT = ? WHERE ID = ?";
        jdbcTemplate.update(UPDATE_SQL,
                bookDto.getUserId(),
                bookDto.getTitle(),
                bookDto.getAuthor(),
                bookDto.getPageCount(),
                bookDto.getId());

        log.info("Update book : {}", bookDto);

        return bookDto;
    }

    @Override
    public Iterable<BookDto> getBookById(Long id) {
        // реализовать недстающие методы
        return null;
    }

    @Override
    public void deleteBookById(Long id) {
        // реализовать недстающие методы
    }
}
