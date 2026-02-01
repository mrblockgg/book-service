package dev.mrblock.data.repository;

import com.zaxxer.hikari.HikariDataSource;
import dev.mrblock.data.mapper.BookMapper;
import dev.mrblock.domain.BookRepository;
import dev.mrblock.domain.Book;
import dev.mrblock.utility.ThrowingConsumer;
import dev.mrblock.utility.ThrowingFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class PostgresBookRepository implements BookRepository {

    private static final String CREATE_BOOK_SQL = "INSERT INTO book (title, author, isbn, genre, publication_year) VALUES (?, ?, ?, ?, ?)";
    private static final String GET_BOOK_SQL = "SELECT * FROM book WHERE id = ?";
    private static final String REMOVE_BOOK_SQL = "DELETE FROM book WHERE id = ?";
    private static final String UPDATE_BOOK_SQL = "UPDATE book SET title = COALESCE(?, title), author = COALESCE(?, author), publication_year = COALESCE(?, publication_year), genre = COALESCE(?, genre) WHERE id = ?";
    private static final String GET_BOOKS_PAGEABLE_SQL = "SELECT * FROM book LIMIT ? OFFSET ? ";

    private final HikariDataSource dataSource;

    @Nullable
    public Book findById(long id) throws SQLException {
        return prepareStatementWithResult(GET_BOOK_SQL, statement -> {
            statement.setLong(1, id);

            ResultSet resultSet = statement.executeQuery();

            return resultSet.next() ? BookMapper.resultSetToBook(resultSet) : null;
        });
    }

    public List<Book> findPageable(long pageSize, long page) throws SQLException {
        return prepareStatementWithResult(GET_BOOKS_PAGEABLE_SQL, statement -> {
            statement.setLong(1, pageSize);
            statement.setLong(2, pageSize * page);

            ResultSet resultSet = statement.executeQuery();
            return BookMapper.resultSetToBooks(resultSet);
        });
    }

    public void insert(Book book) throws SQLException {
        prepareStatement(CREATE_BOOK_SQL, statement -> {
            BookMapper.putBookToStatement(book, statement);
            statement.executeUpdate();
        });
    }

    public void update(long id, String title, String author, Integer publicationYear, String genre) throws SQLException {
        prepareStatement(UPDATE_BOOK_SQL, statement -> {
            BookMapper.updateBook(id, title, author, publicationYear, genre, statement);

            int updatedRows = statement.executeUpdate();
            if (updatedRows != 1) throw new RuntimeException("Book with id %s not found when execute update".formatted(id));
        });
    }

    public void removeById(long id) throws SQLException {
        prepareStatement(REMOVE_BOOK_SQL, statement -> {
            statement.setLong(1, id);

            int updatedRows = statement.executeUpdate();
            if (updatedRows != 1) throw new RuntimeException("Book with id %s not found when execute remove".formatted(id));
        });
    }

    private <T> T prepareStatementWithResult(String sqlQuery, ThrowingFunction<PreparedStatement, T, SQLException> function) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                return function.apply(statement);
            }
        }
    }

    private void prepareStatement(String sqlQuery, ThrowingConsumer<PreparedStatement> consumer) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                consumer.accept(statement);
            }
        }
    }
}
