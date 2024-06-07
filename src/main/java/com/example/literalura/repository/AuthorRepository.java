package com.example.literalura.repository;

import com.example.literalura.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String firstName);
    @Query("SELECT a FROM Author a WHERE a.birth <= :year AND (a.dead > :year OR a.dead IS NULL)")
    List<Author> authorsAliveInYear(@Param("year") int year);

}
