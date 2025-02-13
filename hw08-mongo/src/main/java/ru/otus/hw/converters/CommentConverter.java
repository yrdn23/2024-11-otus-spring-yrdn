package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.Comment;

@Component
public class CommentConverter {

    public String commentToString(Comment comment) {
        return "book: %s, id: %s, text: %s".formatted(
                comment.getBook().getTitle(),
                comment.getId(),
                comment.getText());
    }
}
