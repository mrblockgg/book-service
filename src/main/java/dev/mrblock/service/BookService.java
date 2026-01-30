package dev.mrblock.service;

import com.zaxxer.hikari.HikariDataSource;
import dev.mrblock.entity.Book;
import dev.mrblock.mapper.BookMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookService {

    HikariDataSource dataSource;

    private static final String CREATE_BOOK_SQL = "INSERT INTO book (title, author, isbn, genre, publication_year) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_BOOK_SQL = "SELECT * FROM book WHERE id = ?";
    private static final String REMOVE_BOOK_SQL = "DELETE FROM book WHERE id = ?";
    private static final String UPDATE_BOOK_SQL = "UPDATE book SET title = ?, author = ?, publication_year = ?, genre = ? WHERE id = ?";
    private static final String UPDATE_BOOK_SQL_1 = "UPDATE book SET title = COALESCE(?, title), author = COALESCE(?, author), publication_year = COALESCE(?, publication_year), genre = COALESCE(?, genre) WHERE id = ?";

    @Nullable
    public Book getBookById(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(GET_BOOK_SQL)) {
                statement.setLong(1, id);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String title = resultSet.getString("title");
                    String author = resultSet.getString("author");
                    String isbn = resultSet.getString("isbn");
                    String genre = resultSet.getString("genre");
                    int publicationYear = resultSet.getInt("publication_year");

                    return new Book(id, title, author, isbn, publicationYear, genre);
                } else return null;
            }
        }
    }

    public List<Book> getBooks(int page) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM book LIMIT 10 OFFSET 10 * " + page)) {

                List<Book> pageList = new ArrayList<>();

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    String title = resultSet.getString("title");
                    String author = resultSet.getString("author");
                    String isbn = resultSet.getString("isbn");
                    String genre = resultSet.getString("genre");
                    int publicationYear = resultSet.getInt("publication_year");

                    pageList.add(new Book(id, title, author, isbn, publicationYear, genre));
                }

                return pageList;
            }
        }
    }

    public void createBook(Book book) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(CREATE_BOOK_SQL)) {
                BookMapper.putBookToStatement(book, statement);
                statement.executeUpdate();
            }
        }
    }

    public void updateBook(String title, String author, Integer publicationYear, String genre, long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(UPDATE_BOOK_SQL_1)) {
                statement.setString(1, title);
                statement.setString(2, author);
                statement.setObject(3, publicationYear);
                statement.setString(4, genre);
                statement.setLong(5, id);

                int updatedRows = statement.executeUpdate();
                if (updatedRows != 1) throw new RuntimeException("Не нашли id: " + id);

                log.info("Книга с id: {} Была успешно обновлена", id);
            }
        }
    }

    public void removeBook(long id) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(REMOVE_BOOK_SQL)) {
                statement.setLong(1, id);

                int updatedRows = statement.executeUpdate();
                if (updatedRows != 1) throw new RuntimeException("Не нашли id: " + id);

                log.info("Книга с id: {} Была успешно удалена", id);
            }
        }
    }
}