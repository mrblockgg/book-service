package dev.mrblock.mapper;

import dev.mrblock.entity.Book;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookMapper {

    public static void putBookToStatement(Book book, PreparedStatement statement) throws SQLException {
        statement.setString(1, book.getTitle());
        statement.setString(2, book.getAuthor());
        statement.setString(3, book.getIsbn());
        statement.setString(4, book.getGenre());
        statement.setInt(5, book.getPublicationYear());
    }

}
