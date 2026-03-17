package com.enrique.collectiontracker.comic;

import com.enrique.collectiontracker.common.Format;
import com.enrique.collectiontracker.common.ReadStatus;
import com.enrique.collectiontracker.common.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comics")
public class ComicController {

    private final ComicService comicService;

    public ComicController(ComicService comicService) {
        this.comicService = comicService;
    }

    @GetMapping
    public List<Comic> getAllComics() {
        return comicService.findAll();
    }

    @GetMapping("/{id}")
    public Comic getComicById(@PathVariable Long id) {
        return comicService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comic not found with id: " + id));
    }

    @PostMapping
    public ResponseEntity<Comic> createComic(@Valid @RequestBody Comic comic) {
        Comic savedComic = comicService.save(comic);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComic);
    }

    @PutMapping("/{id}")
    public Comic updateComic(@PathVariable Long id, @Valid @RequestBody Comic comic) {
        if (!comicService.existsById(id)) {
            throw new ResourceNotFoundException("Comic not found with id: " + id);
        }
        comic.setId(id);
        return comicService.save(comic);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComic(@PathVariable Long id) {
        if (!comicService.existsById(id)) {
            throw new ResourceNotFoundException("Comic not found with id: " + id);
        }
        comicService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<Comic> searchComics(@RequestParam String title) {
        return comicService.searchByTitle(title);
    }

    @GetMapping("/filter")
    public List<Comic> filterComics(
            @RequestParam(required = false) ReadStatus status,
            @RequestParam(required = false) Format format) {
        return comicService.filter(status, format);
    }
}
