package com.enrique.collectiontracker.videogame;

import com.enrique.collectiontracker.common.CompletionStatus;
import com.enrique.collectiontracker.common.Format;
import com.enrique.collectiontracker.common.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class VideoGameController {

    private final VideoGameService videoGameService;

    public VideoGameController(VideoGameService videoGameService) {
        this.videoGameService = videoGameService;
    }

    @GetMapping
    public List<VideoGame> getAllGames() {
        return videoGameService.findAll();
    }

    @GetMapping("/{id}")
    public VideoGame getGameById(@PathVariable Long id) {
        return videoGameService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Video game not found with id: " + id));
    }

    @PostMapping
    public ResponseEntity<VideoGame> createGame(@Valid @RequestBody VideoGame videoGame) {
        VideoGame savedGame = videoGameService.save(videoGame);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedGame);
    }

    @PutMapping("/{id}")
    public VideoGame updateGame(@PathVariable Long id, @Valid @RequestBody VideoGame videoGame) {
        if (!videoGameService.existsById(id)) {
            throw new ResourceNotFoundException("Video game not found with id: " + id);
        }
        videoGame.setId(id);
        return videoGameService.save(videoGame);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        if (!videoGameService.existsById(id)) {
            throw new ResourceNotFoundException("Video game not found with id: " + id);
        }
        videoGameService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<VideoGame> searchGames(@RequestParam String title) {
        return videoGameService.searchByTitle(title);
    }

    @GetMapping("/filter")
    public List<VideoGame> filterGames(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) CompletionStatus status,
            @RequestParam(required = false) String console,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) Format format) {
        return videoGameService.filter(genre, status, console, publisher, format);
    }
}
