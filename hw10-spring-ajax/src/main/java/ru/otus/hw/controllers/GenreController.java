package ru.otus.hw.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;

    @GetMapping("/genres/")
    public String genreListPage(Model model) {
        List<Genre> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        return "genreList";
    }

    @GetMapping("/genres/genreEdit")
    public String genreEditPage(@RequestParam("id") long id, Model model) {
        Genre genre = genreService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));
        model.addAttribute("genre", genre);
        return "genreEdit";
    }

    @GetMapping("/genres/genreAdd")
    public String genreAddPage(Model model) {
        model.addAttribute("genre", new Genre());
        return "genreEdit";
    }
}
