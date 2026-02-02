package dev.mrblock.utility;

import com.sun.net.httpserver.HttpExchange;
import dev.mrblock.domain.Book;
import dev.mrblock.exception.BookInvalidException;
import lombok.SneakyThrows;

public class BookValidator {

    public static void validateIsbn(String isbn) throws BookInvalidException {
        if(!isbn.matches("\\d{3}-\\d-\\d{5}-\\d{3}-\\d")) {
            throw new BookInvalidException("isbn", isbn);
        }
    }

    public static void validateTitle(String title) throws BookInvalidException {
        int lengthTitle = title.length();

        if (lengthTitle < 2 || lengthTitle > 100) {
            throw new BookInvalidException("title", lengthTitle);
        }
    }

    public static void validateAuthor(String author) throws BookInvalidException {
        int lengthAuthor = author.length();

        if (lengthAuthor < 2 || lengthAuthor > 50) {
            throw new BookInvalidException("author", lengthAuthor);
        }

    }

    public static void validatePublicationYear(int publicationYear) throws BookInvalidException {
        if (publicationYear < 2000 || publicationYear > 2026) {
            throw new BookInvalidException("publicationYear", publicationYear);
        }

    }

    public static void validateGenre(String genre) throws BookInvalidException {
        for (Book.Genre genreEnum : Book.Genre.values()) {
            if (!genreEnum.name().equals(genre)) {
                throw new BookInvalidException("genre", genreEnum.name());
            }
        }
    }

    public static void validate(Book book) throws BookInvalidException {
        String title = book.getTitle();
        String author = book.getAuthor();
        String isbn = book.getIsbn();
        String genre = book.getGenre();
        Integer publicationYear = book.getPublicationYear();

        validateTitle(title);
        validateAuthor(author);
        validateIsbn(isbn);
        validatePublicationYear(publicationYear);
        validateGenre(genre);
    }
}