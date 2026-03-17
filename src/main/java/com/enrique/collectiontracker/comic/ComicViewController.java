package com.enrique.collectiontracker.comic;

import com.enrique.collectiontracker.common.Format;
import com.enrique.collectiontracker.common.ReadStatus;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comics")
public class ComicViewController {

    private final ComicService comicService;

    public ComicViewController(ComicService comicService) {
        this.comicService = comicService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("comics", comicService.findAll());
        model.addAttribute("pageTitle", "Comics");
        return "comics/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("comic", new Comic());
        model.addAttribute("readStatuses", ReadStatus.values());
        model.addAttribute("formats", Format.values());
        model.addAttribute("pageTitle", "Add Comic");
        return "comics/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute Comic comic, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("readStatuses", ReadStatus.values());
            model.addAttribute("formats", Format.values());
            model.addAttribute("pageTitle", "Add Comic");
            return "comics/form";
        }
        comicService.save(comic);
        return "redirect:/comics";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Comic comic = comicService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comic Id: " + id));
        model.addAttribute("comic", comic);
        model.addAttribute("pageTitle", comic.getTitle());
        return "comics/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Comic comic = comicService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid comic Id: " + id));
        model.addAttribute("comic", comic);
        model.addAttribute("readStatuses", ReadStatus.values());
        model.addAttribute("formats", Format.values());
        model.addAttribute("pageTitle", "Edit Comic");
        return "comics/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute Comic comic, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("readStatuses", ReadStatus.values());
            model.addAttribute("formats", Format.values());
            model.addAttribute("pageTitle", "Edit Comic");
            return "comics/form";
        }
        comic.setId(id);
        comicService.save(comic);
        return "redirect:/comics";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        comicService.deleteById(id);
        return "redirect:/comics";
    }
}
