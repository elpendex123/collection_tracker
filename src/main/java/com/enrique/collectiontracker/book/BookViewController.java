package com.enrique.collectiontracker.book;

import com.enrique.collectiontracker.common.Format;
import com.enrique.collectiontracker.common.ReadStatus;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BookViewController {

    private final BookService bookService;

    public BookViewController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("books", bookService.findAll());
        model.addAttribute("pageTitle", "Books");
        return "books/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("readStatuses", ReadStatus.values());
        model.addAttribute("formats", Format.values());
        model.addAttribute("pageTitle", "Add Book");
        return "books/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute Book book, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("readStatuses", ReadStatus.values());
            model.addAttribute("formats", Format.values());
            model.addAttribute("pageTitle", "Add Book");
            return "books/form";
        }
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id: " + id));
        model.addAttribute("book", book);
        model.addAttribute("pageTitle", book.getTitle());
        return "books/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id: " + id));
        model.addAttribute("book", book);
        model.addAttribute("readStatuses", ReadStatus.values());
        model.addAttribute("formats", Format.values());
        model.addAttribute("pageTitle", "Edit Book");
        return "books/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute Book book, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("readStatuses", ReadStatus.values());
            model.addAttribute("formats", Format.values());
            model.addAttribute("pageTitle", "Edit Book");
            return "books/form";
        }
        book.setId(id);
        bookService.save(book);
        return "redirect:/books";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }
}
