package com.enrique.collectiontracker.videogame;

import com.enrique.collectiontracker.common.CompletionStatus;
import com.enrique.collectiontracker.common.Format;
import org.springframework.data.jpa.domain.Specification;

public class VideoGameSpecifications {

    public static Specification<VideoGame> hasGenre(String genre) {
        return (root, query, criteriaBuilder) -> {
            if (genre == null || genre.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(criteriaBuilder.lower(root.get("genre")), genre.toLowerCase());
        };
    }

    public static Specification<VideoGame> hasStatus(CompletionStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("completionStatus"), status);
        };
    }

    public static Specification<VideoGame> hasConsole(String console) {
        return (root, query, criteriaBuilder) -> {
            if (console == null || console.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(criteriaBuilder.lower(root.get("console")), console.toLowerCase());
        };
    }

    public static Specification<VideoGame> hasPublisher(String publisher) {
        return (root, query, criteriaBuilder) -> {
            if (publisher == null || publisher.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(criteriaBuilder.lower(root.get("publisher")), publisher.toLowerCase());
        };
    }

    public static Specification<VideoGame> hasFormat(Format format) {
        return (root, query, criteriaBuilder) -> {
            if (format == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("format"), format);
        };
    }
}
