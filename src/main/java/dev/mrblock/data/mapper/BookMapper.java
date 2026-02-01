package dev.mrblock.data.mapper;

import dev.mrblock.domain.Book;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class BookMapper {

    public static void putBookToStatement(Book book, PreparedStatement statement) throws SQLException {
        statement.setString(1, book.getTitle());
        statement.setString(2, book.getAuthor());

        String regex = "\\d{3}-\\d-\\d{5}-\\d{3}-\\d";
        String isbn = book.getIsbn();
        boolean isValid = isbn.matches(regex);

        if (!isValid) {
            throw new RuntimeException("don't valid ISBN");
        }

        statement.setString(3, isbn);
        statement.setString(4, book.getGenre());
        statement.setInt(5, book.getPublicationYear());
    }

    public static void updateBook(long id,
                                  String title,
                                  String author,
                                  Integer publicationYear,
                                  String genre,
                                  PreparedStatement statement
    ) throws SQLException {
        int lengthTitle = title.length();

        if (lengthTitle < 2 || lengthTitle > 100) {
            throw new RuntimeException("Author book > 100 or < 2");
        }

        statement.setString(1, title);
        int lengthAuthor = author.length();

        if (lengthAuthor < 2 || lengthAuthor > 50) {
            throw new RuntimeException("Author book > 50 or < 2");
        }

        statement.setString(2, author);

        if (publicationYear < 2000 || publicationYear > 2026) {
            throw new RuntimeException("publication year book > 2026 or < 2000");
        }

        statement.setObject(3, publicationYear);

        for (Book.Genre genreEnum : Book.Genre.values()) {
            if (!genreEnum.name().equals(genre)) {
                throw new RuntimeException("Incorrect write genre");
            }
        }

        statement.setString(4, genre);
        statement.setLong(5, id);
    }

    public static Book resultSetToBook(ResultSet resultSet) throws SQLException {
        long id = resultSet.getLong("id");
        String title = resultSet.getString("title");
        String author = resultSet.getString("author");
        String isbn = resultSet.getString("isbn");
        String genre = resultSet.getString("genre");
        int publicationYear = resultSet.getInt("publication_year");

        return new Book(id, title, author, isbn, publicationYear, genre);
    }

    public static List<Book> resultSetToBooks(ResultSet resultSet) throws SQLException {
        LinkedList<Book> books = new LinkedList<>();

        while (resultSet.next()) {
            Book book = resultSetToBook(resultSet);
            books.add(book);
        }

        return books;
    }

}
