package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.BadRequestException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BookServiceImplJpa implements BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookMapper bookMapper;
    private final UserMapper userMapper;

    public BookServiceImplJpa(BookRepository bookRepository, UserRepository userRepository, BookMapper bookMapper, UserMapper userMapper) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.bookMapper = bookMapper;
        this.userMapper = userMapper;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        Book book = bookMapper.bookDtoToBook(bookDto);
        log.info("Mapped bookDto: {}", bookDto);

        Person person = userRepository.findById(bookDto.getUserId())
                .orElseThrow(() -> new BadRequestException("id person not found"));
        log.info("Get person from bd: {}", person);

        book.setPerson(person);
        Book bookResponse = bookRepository.save(book);
        log.info("Save book : {}", book);

        bookDto.setId(bookResponse.getId());
        return bookDto;
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        if (bookDto.getId() < 1) throw new BadRequestException("Bad request");

        Book book = bookRepository.findById(bookDto.getId())
                .orElseThrow(() -> new BadRequestException("id book not found"));
        log.info("Get book from bd: {}", book);

        Person person = userRepository.findById(bookDto.getUserId())
                .orElseThrow(() -> new BadRequestException("id person not found"));
        log.info("Get person from bd: {}", person);

        book = bookMapper.bookDtoToBook(bookDto);
        log.info("Update book: {}", bookDto);

        book.setPerson(person);
        Book bookResponse = bookRepository.save(book);
        log.info("Save update book : {}", book);

        bookDto.setId(bookResponse.getId());
        return bookDto;
    }

    @Override
    public BookDto getBookById(Long id) {
        return null;
    }

    @Override
    public void deleteBookById(Long id) {

    }
}