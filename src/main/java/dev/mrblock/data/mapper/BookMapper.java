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
        statement.setString(3, book.getIsbn());
        statement.setString(4, book.getGenre());
        statement.setInt(5, book.getPublicationYear());
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
