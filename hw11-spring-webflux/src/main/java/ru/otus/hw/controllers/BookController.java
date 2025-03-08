package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class BookController {

    @GetMapping("/books/")
    public String bookListPage() {
        return "bookList";
    }

    @GetMapping("/books/bookEdit")
    public String bookEditPage(@RequestParam("id") String id, Model model) {
        model.addAttribute("id", id);
        return "bookEdit";
    }

    @GetMapping("/books/bookAdd")
    public String bookAddPage() {
        return "bookAdd";
    }

    @GetMapping("/books/bookDelete")
    public String bookDeletePage(@RequestParam("id") String id, Model model) {
        model.addAttribute("id", id);
        return "bookDelete";
    }
}
