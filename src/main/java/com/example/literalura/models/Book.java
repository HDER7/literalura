package com.example.literalura.models;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {
    @Id
    private long id;
    @Column(unique = true, nullable = false)
    private String title;
    @ManyToOne()
    private Author author;
    @Enumerated(EnumType.STRING)
    private Languages language;
    private long downloads;

    public Book(BookData b, Author a) {
        this.id = b.id();
        this.title = b.title();
        this.author = a;
        this.language = Languages.fromString(b.language().getFirst());
        this.downloads = b.downloads();
    }

    public Book() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Languages getLanguage() {
        return language;
    }

    public void setLanguage(Languages language) {
        this.language = language;
    }

    public long getDownloads() {
        return downloads;
    }

    public void setDownloads(long downloads) {
        this.downloads = downloads;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return String.format(
                """
                ~~~~~~~~~~~~~~~~~~~LIBRO~~~~~~~~~~~~~~~~~~~
                Título      : %s
                Autor       : %s
                Idioma      : %s
                Descargas   : %d
                ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                """,
                getTitle(),
                getAuthor().getName(),
                getLanguage(),
                getDownloads()
        );
    }
}
