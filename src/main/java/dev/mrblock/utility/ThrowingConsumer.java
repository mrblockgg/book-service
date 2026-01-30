package dev.mrblock.utility;

import java.sql.SQLException;

@FunctionalInterface
public interface ThrowingConsumer<T> {
    void accept(T t) throws SQLException;
}
