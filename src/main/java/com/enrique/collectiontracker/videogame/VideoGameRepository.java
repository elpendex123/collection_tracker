package com.enrique.collectiontracker.videogame;

import com.enrique.collectiontracker.common.CompletionStatus;
import com.enrique.collectiontracker.common.Format;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoGameRepository extends JpaRepository<VideoGame, Long>, JpaSpecificationExecutor<VideoGame> {

    List<VideoGame> findByTitleContainingIgnoreCase(String title);

    List<VideoGame> findByGenreIgnoreCase(String genre);

    List<VideoGame> findByCompletionStatus(CompletionStatus status);

    List<VideoGame> findByConsoleIgnoreCase(String console);

    List<VideoGame> findByPublisherIgnoreCase(String publisher);

    List<VideoGame> findByFormat(Format format);

    boolean existsByIsbnAndTitleIgnoreCase(String isbn, String title);
}
