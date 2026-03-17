package com.enrique.collectiontracker.videogame;

import com.enrique.collectiontracker.common.CompletionStatus;
import com.enrique.collectiontracker.common.Format;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideoGameService {

    private final VideoGameRepository videoGameRepository;

    public VideoGameService(VideoGameRepository videoGameRepository) {
        this.videoGameRepository = videoGameRepository;
    }

    public List<VideoGame> findAll() {
        return videoGameRepository.findAll();
    }

    public Optional<VideoGame> findById(Long id) {
        return videoGameRepository.findById(id);
    }

    public VideoGame save(VideoGame videoGame) {
        return videoGameRepository.save(videoGame);
    }

    public void deleteById(Long id) {
        videoGameRepository.deleteById(id);
    }

    public List<VideoGame> searchByTitle(String title) {
        return videoGameRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<VideoGame> filter(String genre, CompletionStatus status, String console, String publisher, Format format) {
        Specification<VideoGame> spec = Specification.where(VideoGameSpecifications.hasGenre(genre))
                .and(VideoGameSpecifications.hasStatus(status))
                .and(VideoGameSpecifications.hasConsole(console))
                .and(VideoGameSpecifications.hasPublisher(publisher))
                .and(VideoGameSpecifications.hasFormat(format));
        return videoGameRepository.findAll(spec);
    }

    public boolean existsById(Long id) {
        return videoGameRepository.existsById(id);
    }
}
