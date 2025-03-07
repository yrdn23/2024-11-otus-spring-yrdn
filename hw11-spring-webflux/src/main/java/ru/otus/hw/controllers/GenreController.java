package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class GenreController {

    @GetMapping("/genres/")
    public String genreListPage() {
        return "genreList";
    }

    @GetMapping("/genres/genreEdit")
    public String genreEditPage(@RequestParam("id") String id, Model model) {
        model.addAttribute("id", id);
        return "genreEdit";
    }

    @GetMapping("/genres/genreAdd")
    public String genreAddPage() {
        return "genreEdit";
    }
}
