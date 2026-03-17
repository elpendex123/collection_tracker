package com.enrique.collectiontracker.comic;

import com.enrique.collectiontracker.common.Format;
import com.enrique.collectiontracker.common.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComicRepository extends JpaRepository<Comic, Long>, JpaSpecificationExecutor<Comic> {

    List<Comic> findByTitleContainingIgnoreCase(String title);

    List<Comic> findByReadStatus(ReadStatus status);

    List<Comic> findByFormat(Format format);

    List<Comic> findBySeriesContainingIgnoreCase(String series);

    boolean existsByIsbnAndTitleIgnoreCase(String isbn, String title);
}
