package ru.otus.hw.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;

@Controller
@RequiredArgsConstructor
public class CommentController {

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

    @GetMapping("/comments/commentEdit")
    public String commentEditPage(@RequestParam("id") long id, Model model) {
        var comment = commentService.findById(id)
                .orElseThrow(()  -> new EntityNotFoundException("Comment not found"));
        model.addAttribute("comment", comment);
        model.addAttribute("book", comment.getBook());
        return "commentEdit";
    }

    @PostMapping("/comments/commentEdit")
    public String commentSave(@ModelAttribute("comment") Comment comment, @RequestParam("book_id") long bookId,
                              BindingResult bindingResult, Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "commentEdit";
        }
        var book = bookService.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        comment.setBook(book);
        commentService.save(comment);
        return "redirect:/comments/?id=%s".formatted(bookId);
    }

    @GetMapping("/comments/commentAdd")
    public String commentAddPage(@RequestParam("book_id") long bookId, Model model) {
        var comment = new Comment();
        var book = bookService.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        comment.setBook(book);
        model.addAttribute("comment", comment);
        model.addAttribute("book", book);
        return "commentEdit";
    }

    @GetMapping("/comments/commentDelete")
    public String commentDeletePage(@RequestParam("id") long id, Model model) {
        var comment = commentService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        var book = bookService.findById(comment.getBook().getId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        model.addAttribute("comment", comment);
        model.addAttribute("book", book);
        return "commentDelete";
    }

    @PostMapping("/comments/commentDelete")
    public String commentDelete(@Valid @ModelAttribute("comment") Comment comment, @RequestParam("book_id") long bookId,
                                BindingResult bindingResult, Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "commentDelete";
        }
        commentService.deleteById(comment.getId());
        return "redirect:/comments/?id=%s".formatted(bookId);
    }
}
