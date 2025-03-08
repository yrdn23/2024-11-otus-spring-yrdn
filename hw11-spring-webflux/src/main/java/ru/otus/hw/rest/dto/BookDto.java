package ru.otus.hw.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookDto {

    private String id;

    private String title;

    private String authorId;

    private String authorFullName;

    private String genreId;

    private String genreName;
}
