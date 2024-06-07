package com.example.literalura.main;

import com.example.literalura.models.*;
import com.example.literalura.repository.AuthorRepository;
import com.example.literalura.repository.BookRepository;
import com.example.literalura.service.Converter;
import com.example.literalura.service.GutexAPI;

import java.util.*;

public class Main {
    private final Scanner key = new Scanner(System.in);
    private final GutexAPI api = new GutexAPI();
    private final BookRepository repository;
    private final AuthorRepository authorRepository;
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private final Converter converter = new Converter();

    public Main(BookRepository repository, AuthorRepository authorRepository) {
        this.repository = repository;
        this.authorRepository = authorRepository;
    }

    public void menu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                \n
                =========================================
                |              LITERALURA               |
                =========================================
                | 1 - Todos los libros                  |
                | 2 - Buscar libro por título           |
                | 3 - Todos los autores                 |
                | 4 - Autores vivos en determinado año  |
                | 5 - Libros por idioma                 |
                | 6 - Top 10 libros                     |
                | 7 - Autor y sus obras                 |
                | 8 - Buscar Autor por nombre           |
                | 0 - Salir                             |
                =========================================
                """;
            System.out.println(menu);
            opcion = key.nextInt();
            key.nextLine();

            switch (opcion) {
                case 1:
                    allBooks();
                    break;
                case 2:
                    titleBook();
                    break;
                case 3:
                    allAuthors();
                    break;
                case 4:
                    livingAuthors();
                    break;
                case 5:
                    bookLanguage();
                    break;
                case 6:
                    top10Books();
                    break;
                case 7:
                    authorBook();
                    break;
                case 8:
                    searchAuthor();
                    break;
                case 0:
                    System.out.println("Cerrando Consola...");
                    break;
                default:
                    System.out.println("Opción inválida\n");
            }
        }

    }

    private void setAB(List <Author> authors){
        authors.forEach(author -> {
            author.setBooks(repository.booksAuthor(author.getName()));
        });
    }

    private Book getDataBD(String title){
        Optional<Book> bookOptional = repository.findByTitleContainsIgnoreCase(title);
        return bookOptional.orElse(null);
    }
    private Book getDataAPI(String title){
        var search = (URL_BASE +title.replace(" ","%20"));
        var json = api.getData(search);
        ApiData res = converter.getData(json, ApiData.class);
        Optional<BookData> bookDataOptional = res.books().stream()
                .filter(b -> b.title().equalsIgnoreCase(title))
                .findFirst();
        if (bookDataOptional.isPresent()) {
            BookData bookData = bookDataOptional.get();
            AuthorData authorData = bookData.author().getFirst();
            Book book = new Book(bookData, new Author(authorData));
            Author author = book.getAuthor();
            Optional<Author> existingAuthor = authorRepository.findByName(author.getName());
            if (existingAuthor.isEmpty()) {
                authorRepository.save(author);
            } else {
                book.setAuthor(existingAuthor.get());
            }
            repository.save(book);
            return new Book(bookData, new Author(authorData));
        }
        return null;
    }


    private Book getBook(){
        try{
            System.out.println("\nIngresa el titulo del libro que deseas buscar: ");
            String title = key.nextLine();
            Book Book = getDataBD(title);
            if (Book == null) {
                Book = getDataAPI(title);
            }
            return Book;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void allBooks() {
        List<Book> books = repository.findAll();
        books.forEach(System.out::println);
    }

    //Metodo para buscar un libro por su titulo
    private void titleBook() {
        Book book = getBook();
        if (book!=null) {
            System.out.println("¡Libro encontrado!\n");
            System.out.println(book);
        }
        else{
            System.out.println("No se encontro el libro\n");
        }
    }

    private void allAuthors() {
        List<Author> authors = authorRepository.findAll();
        setAB(authors);
        authors.forEach(System.out::println);
    }

    //Metodo para encontrar los autores vivos en determinado año
    private void livingAuthors() {
        try{
            System.out.println("Ingresa el año en que deseas saber los autores vivos");
            int year = key.nextInt();
            List<Author> authors = authorRepository.authorsAliveInYear(year);
            setAB(authors);
            if (authors.isEmpty()){
                System.out.println("\nNo hay autores vivos en año "+ year);
            }else {
                System.out.println("===========Autores vivos en el año "+year+"=============");
                authors.forEach(System.out::println);
            }
        }catch (NumberFormatException e){
            System.out.println(e.getMessage());
        }
    }

    //Listado de libros por idioma
    private void bookLanguage() {
        try {
            System.out.println("====== Listado de Idiomas Disponibles ======");
            for (Languages language : Languages.values()) {
                System.out.println("- " + language.getAbreviature() + ": " + language.getDisplayName());
            }
            System.out.println("============================================");
            System.out.println("Escribe la abreviaruta del idioma: ");
            var l = key.nextLine();
            var language = Languages.fromString(l);
            List<Book> books = repository.findByLanguage(language);
            if (books.isEmpty()){
                System.out.println("No se encuentran libros en el idioma " + language.getDisplayName());
            }
            else {
                System.out.println("=============Libros en "+language.getDisplayName()+"==============");
                books.forEach(System.out::println);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void top10Books() {
        List<Book> top = repository.top10Books();
        System.out.println("========Top 10 Libros Más Descargados======");
        top.forEach(System.out::println);
    }

    private void authorBook() {
        try {
            System.out.println("Ingresa el nombre del autor que buscar sus obras: ");
            String name = key.nextLine();
            Optional<Author> author = authorRepository.findByName(name);
            if(author.isPresent()) {
                List<Book> books = repository.booksAuthor(name);
                System.out.println("=============Libros de " + name + "==============");
                books.forEach(System.out::println);
            }
            else{
                System.out.println("El autor no se encuentra");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    //Busca un autor segun su nombre en la BD (Nombre exacto)
    private void searchAuthor() {
        System.out.println("Ingresa el nombre del autor que deseas buscar: ");
        String name = key.nextLine();
        Optional<Author> author = authorRepository.findByName(name);
        if (author.isPresent()){
            Author a = author.get();
            a.setBooks(repository.booksAuthor(a.getName()));
            System.out.println(a);
        }
        else {
            System.out.println("El autor no se encuentra");
        }
    }
}
