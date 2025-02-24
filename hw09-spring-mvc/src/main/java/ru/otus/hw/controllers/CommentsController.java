package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

@Controller
@RequiredArgsConstructor
public class CommentsController {

    private final BookService bookService;

    private final CommentService commentService;

    @GetMapping("/comments/")
    public String commentListPage(@RequestParam("id") long bookId, Model model) {
        var comments = commentService.findByBookId(bookId);
        model.addAttribute("comments", comments);
        model.addAttribute("book", bookService.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found")));
        return "commentList";
    }

}
