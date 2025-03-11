package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CommentController {

    @GetMapping("/comments/")
    public String commentListPage(@RequestParam("id") String bookId, Model model) {
        model.addAttribute("bookId", bookId);
        return "commentList";
    }
}
