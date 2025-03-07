package ru.otus.hw.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.otus.hw.models.Genre;

@Data
@AllArgsConstructor
public class GenreDto {

    private String id;

    @Length(min = 3)
    private String name;

    public static GenreDto toDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }

    public Genre toDomainObject() {
        return new Genre("".equals(id) ? null : id, name);
    }
}
