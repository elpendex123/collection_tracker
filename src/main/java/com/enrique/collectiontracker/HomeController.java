package com.enrique.collectiontracker;

import com.enrique.collectiontracker.book.BookService;
import com.enrique.collectiontracker.comic.ComicService;
import com.enrique.collectiontracker.videogame.VideoGameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final BookService bookService;
    private final ComicService comicService;
    private final VideoGameService videoGameService;

    public HomeController(BookService bookService, ComicService comicService, VideoGameService videoGameService) {
        this.bookService = bookService;
        this.comicService = comicService;
        this.videoGameService = videoGameService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("bookCount", bookService.findAll().size());
        model.addAttribute("comicCount", comicService.findAll().size());
        model.addAttribute("gameCount", videoGameService.findAll().size());
        model.addAttribute("pageTitle", "Home");
        return "index";
    }
}
