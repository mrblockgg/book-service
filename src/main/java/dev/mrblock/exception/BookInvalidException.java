package dev.mrblock.exception;

public class BookInvalidException extends Exception {

    public BookInvalidException(String field, Object value) {
        super("Invalid value '%s' for field '%s'".formatted(value, field));
    }

}
