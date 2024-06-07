package com.example.literalura.models;


import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, nullable = false)
    private String name;
    private int birth;
    private int dead;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Book> books;

    public Author(AuthorData a) {
        this.name = a.name().replace(",","");
        this.birth = a.birth();
        this.dead = a.dead();
    }

    public Author() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirth() {
        return birth;
    }

    public void setBirth(int birth) {
        this.birth = birth;
    }

    public int getDead() {
        return dead;
    }

    public void setDead(int dead) {
        this.dead = dead;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public List<String> getBooks() {
        return (List<String>) books.stream().map(Book::getTitle).toList();
    }

    @Override
    public String toString() {
        return String.format(
                """
                ~~~~~~~~~AUTOR~~~~~~~~
                Nombre: %s
                Nacimiento: %d
                Fallecimiento: %d
                Libros: %s
                ~~~~~~~~~~~~~~~~~~~~~~
                """,
                getName(), getBirth(), getDead(),getBooks()
        );
    }
}
