package ru.otus.hw.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Comment;

@Data
@AllArgsConstructor
public class CommentDto {

    private String id;

    private String text;

    private String bookId;

    private String bookTitle;

    public static CommentDto toDto(Comment comment) {
        return new CommentDto(comment.getId(), comment.getText(),
                comment.getBook().getId(), comment.getBook().getTitle());
    }
}
