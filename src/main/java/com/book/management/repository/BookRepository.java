package com.book.management.repository;

import com.book.management.model.Book;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {
    Page<Book> findAll(Pageable pageable);

    Optional<Book> findBookByIsbn(String isbn);

    Optional<Book> findBookById(Long id);
}
