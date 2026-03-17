package com.enrique.collectiontracker.comic;

import com.enrique.collectiontracker.common.Format;
import com.enrique.collectiontracker.common.ReadStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComicService {

    private final ComicRepository comicRepository;

    public ComicService(ComicRepository comicRepository) {
        this.comicRepository = comicRepository;
    }

    public List<Comic> findAll() {
        return comicRepository.findAll();
    }

    public Optional<Comic> findById(Long id) {
        return comicRepository.findById(id);
    }

    public Comic save(Comic comic) {
        return comicRepository.save(comic);
    }

    public void deleteById(Long id) {
        comicRepository.deleteById(id);
    }

    public List<Comic> searchByTitle(String title) {
        return comicRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Comic> filter(ReadStatus status, Format format) {
        Specification<Comic> spec = Specification.where(ComicSpecifications.hasStatus(status))
                .and(ComicSpecifications.hasFormat(format));
        return comicRepository.findAll(spec);
    }

    public boolean existsById(Long id) {
        return comicRepository.existsById(id);
    }
}
