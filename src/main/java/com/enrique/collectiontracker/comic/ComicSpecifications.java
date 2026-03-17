package com.enrique.collectiontracker.comic;

import com.enrique.collectiontracker.common.Format;
import com.enrique.collectiontracker.common.ReadStatus;
import org.springframework.data.jpa.domain.Specification;

public class ComicSpecifications {

    public static Specification<Comic> hasStatus(ReadStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("readStatus"), status);
        };
    }

    public static Specification<Comic> hasFormat(Format format) {
        return (root, query, criteriaBuilder) -> {
            if (format == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("format"), format);
        };
    }
}
