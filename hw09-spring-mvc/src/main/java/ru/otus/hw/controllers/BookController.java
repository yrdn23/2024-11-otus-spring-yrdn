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
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final AuthorService authorService;

    private final GenreService genreService;

    private final BookService bookService;

    @GetMapping("/books/")
    public String bookListPage(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "bookList";
    }

    @GetMapping("/books/bookEdit")
    public String bookEditPage(@RequestParam("id") long id, Model model) {
        Book book = bookService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        model.addAttribute("book", book);
        List<Author> authors = authorService.findAll();
        model.addAttribute("authors", authors);
        List<Genre> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        return "bookEdit";
    }

    @PostMapping("/books/bookEdit")
    public String bookSave(@Valid @ModelAttribute("book") Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "bookEdit";
        }
        book.setAuthor(authorService.findById(book.getAuthor().getId())
                .orElseThrow(() -> new EntityNotFoundException("Author not found")));
        book.setGenre(genreService.findById(book.getGenre().getId())
                .orElseThrow(() -> new EntityNotFoundException("Genre not found")));
        bookService.save(book);
        return "redirect:/books/";
    }

    @GetMapping("/books/bookAdd")
    public String bookAddPage(Model model) {
        model.addAttribute("book", new Book());
        List<Author> authors = authorService.findAll();
        model.addAttribute("authors", authors);
        List<Genre> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        return "bookEdit";
    }

    @GetMapping("/books/bookDelete")
    public String bookDeletePage(@RequestParam("id") long id, Model model) {
        Book book = bookService.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found"));
        model.addAttribute("book", book);
        return "bookDelete";
    }

    @PostMapping("/books/bookDelete")
    public String bookDelete(@Valid @ModelAttribute("book") Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "bookDelete";
        }
        bookService.deleteById(book.getId());
        return "redirect:/books/";
    }
}
