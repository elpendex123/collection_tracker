package com.enrique.collectiontracker.videogame;

import com.enrique.collectiontracker.common.CompletionStatus;
import com.enrique.collectiontracker.common.Format;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/games")
public class VideoGameViewController {

    private final VideoGameService videoGameService;

    public VideoGameViewController(VideoGameService videoGameService) {
        this.videoGameService = videoGameService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("games", videoGameService.findAll());
        model.addAttribute("pageTitle", "Video Games");
        return "games/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("game", new VideoGame());
        model.addAttribute("completionStatuses", CompletionStatus.values());
        model.addAttribute("formats", Format.values());
        model.addAttribute("pageTitle", "Add Game");
        return "games/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("game") VideoGame game, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("completionStatuses", CompletionStatus.values());
            model.addAttribute("formats", Format.values());
            model.addAttribute("pageTitle", "Add Game");
            return "games/form";
        }
        videoGameService.save(game);
        return "redirect:/games";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        VideoGame game = videoGameService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid game Id: " + id));
        model.addAttribute("game", game);
        model.addAttribute("pageTitle", game.getTitle());
        return "games/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        VideoGame game = videoGameService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid game Id: " + id));
        model.addAttribute("game", game);
        model.addAttribute("completionStatuses", CompletionStatus.values());
        model.addAttribute("formats", Format.values());
        model.addAttribute("pageTitle", "Edit Game");
        return "games/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute("game") VideoGame game, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("completionStatuses", CompletionStatus.values());
            model.addAttribute("formats", Format.values());
            model.addAttribute("pageTitle", "Edit Game");
            return "games/form";
        }
        game.setId(id);
        videoGameService.save(game);
        return "redirect:/games";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        videoGameService.deleteById(id);
        return "redirect:/games";
    }
}
