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
import ru.otus.hw.models.Author;
import ru.otus.hw.services.AuthorService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("/authors/")
    public String authorListPage(Model model) {
        List<Author> authors = authorService.findAll();
        model.addAttribute("authors", authors);
        return "authorList";
    }

    @GetMapping("/authors/authorEdit")
    public String authorEditPage(@RequestParam("id") long id, Model model) {
        Author author = authorService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));
        model.addAttribute("author", author);
        return "authorEdit";
    }

    @PostMapping("/authors/authorEdit")
    public String authorSave(@Valid @ModelAttribute("author") Author author, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "authorEdit";
        }
        authorService.save(author);
        return "redirect:/authors/";
    }

    @GetMapping("/authors/authorAdd")
    public String authorAddPage(Model model) {
        model.addAttribute("author", new Author());
        return "authorEdit";
    }
}
