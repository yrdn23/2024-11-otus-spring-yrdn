package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    @GetMapping("/authors/")
    public String authorListPage() {
        return "authorList";
    }

    @GetMapping("/authors/authorEdit")
    public String authorEditPage(@RequestParam("id") String id, Model model) {
        model.addAttribute("id", id);
        return "authorEdit";
    }

    @GetMapping("/authors/authorAdd")
    public String authorAddPage() {
        return "authorEdit";
    }
}
