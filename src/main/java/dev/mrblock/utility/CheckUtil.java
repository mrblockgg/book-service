package dev.mrblock.utility;

import com.sun.net.httpserver.HttpExchange;
import dev.mrblock.domain.Book;
import lombok.SneakyThrows;

public class CheckUtil {

    public static boolean isValidRegex(String isbn) {
        return isbn.matches("\\d{3}-\\d-\\d{5}-\\d{3}-\\d");
    }

    public static void checkTitle(String title) {
        int lengthTitle = title.length();

        if (lengthTitle < 2 || lengthTitle > 100) {
            throw new RuntimeException("Author book > 100 or < 2");
        }
    }

    public static void checkAuthor(String author) {
        int lengthAuthor = author.length();

        if (lengthAuthor < 2 || lengthAuthor > 50) {
            throw new RuntimeException("Author book > 50 or < 2");
        }

    }

    public static void checkPublicationYear(int publicationYear) {
        if (publicationYear < 2000 || publicationYear > 2026) {
            throw new RuntimeException("publication year book > 2026 or < 2000");
        }

    }

    public static void checkGenre(String genre) {
        for (Book.Genre genreEnum : Book.Genre.values()) {
            if (!genreEnum.name().equals(genre)) {
                throw new RuntimeException("Incorrect write genre");
            }
        }
    }

    @SneakyThrows
    public static void check(Book book) {
        String title = book.getTitle();
        String author = book.getAuthor();
        String isbn = book.getIsbn();
        String genre = book.getGenre();
        Integer publicationYear = book.getPublicationYear();

        checkTitle(title);
        checkAuthor(author);

        if (!isValidRegex(isbn)) {
            throw new RuntimeException("don't valid ISBN");
        }

        checkPublicationYear(publicationYear);
        checkGenre(genre);
    }

    @SneakyThrows
    public static void validateBookAndSendErrorIfInvalid(Book book, HttpExchange httpExchange) {
        try {
            check(book);
        } catch (RuntimeException e) {
            ExchangeUtil.sendCodeResponse(httpExchange, 405);
        }
    }
}