package com.enrique.collectiontracker.book;

import com.enrique.collectiontracker.common.Format;
import com.enrique.collectiontracker.common.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByGenreIgnoreCase(String genre);

    List<Book> findByReadStatus(ReadStatus status);

    List<Book> findByFormat(Format format);

    boolean existsByIsbnAndTitleIgnoreCase(String isbn, String title);
}
