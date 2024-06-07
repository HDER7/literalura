package com.example.literalura.repository;

import com.example.literalura.models.Book;
import com.example.literalura.models.Languages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitleContainsIgnoreCase(String name);
    List<Book> findByLanguage(Languages language);
    @Query("SELECT b FROM Book b ORDER BY b.downloads DESC LIMIT 10")
    List<Book> top10Books();
}
