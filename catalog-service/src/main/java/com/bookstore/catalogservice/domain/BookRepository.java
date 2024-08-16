package com.bookstore.catalogservice.domain;

import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BookRepository extends CrudRepository<Book,Long> {

    Optional<Book> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);

    @Transactional
    void deleteByIsbn(String isbn);

}