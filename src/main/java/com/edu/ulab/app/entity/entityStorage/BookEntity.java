package com.edu.ulab.app.entity.entityStorage;

import lombok.Data;

@Data
public class BookEntity {
    private Long id;

    // ManyToOne
    // Двух стороння связь.
    private Long userId;
    private String title;
    private String author;
    private long pageCount;
}
