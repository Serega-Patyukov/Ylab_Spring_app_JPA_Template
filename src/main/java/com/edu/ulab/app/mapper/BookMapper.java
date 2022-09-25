package com.edu.ulab.app.mapper;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.entityStorage.BookEntity;
import com.edu.ulab.app.entity.entityJpaTemplate.Book;
import com.edu.ulab.app.web.request.BookRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDto bookRequestToBookDto(BookRequest bookRequest);

    BookRequest bookDtoToBookRequest(BookDto bookDto);

    Book bookDtoToBook(BookDto bookDto);

    BookDto bookToBookDto(Book book);

    BookEntity bookDtoToBookEntity(BookDto bookDto);

    BookDto bookEntityToBookDot(BookEntity bookEntity);

    BookEntity bookEntityToBookEntity(BookEntity bookEntity);
}
