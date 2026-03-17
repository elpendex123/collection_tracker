package com.enrique.collectiontracker.book;

import com.enrique.collectiontracker.common.Format;
import com.enrique.collectiontracker.common.ReadStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    public List<Book> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> filter(String genre, ReadStatus status, Format format) {
        Specification<Book> spec = Specification.where(BookSpecifications.hasGenre(genre))
                .and(BookSpecifications.hasStatus(status))
                .and(BookSpecifications.hasFormat(format));
        return bookRepository.findAll(spec);
    }

    public boolean existsById(Long id) {
        return bookRepository.existsById(id);
    }
}
