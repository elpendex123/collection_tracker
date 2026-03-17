package com.enrique.collectiontracker.book;

import com.enrique.collectiontracker.common.Format;
import com.enrique.collectiontracker.common.ReadStatus;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecifications {

    public static Specification<Book> hasGenre(String genre) {
        return (root, query, criteriaBuilder) -> {
            if (genre == null || genre.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(criteriaBuilder.lower(root.get("genre")), genre.toLowerCase());
        };
    }

    public static Specification<Book> hasStatus(ReadStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("readStatus"), status);
        };
    }

    public static Specification<Book> hasFormat(Format format) {
        return (root, query, criteriaBuilder) -> {
            if (format == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("format"), format);
        };
    }
}
