package com.edu.ulab.app.web.request;

import lombok.Data;

@Data
public class BookRequest {
    private long id;

    private String title;
    private String author;
    private long pageCount;
}
