package dev.mrblock.infrastructure.http;

import com.sun.net.httpserver.HttpExchange;
import dev.mrblock.exception.BookInvalidException;

import java.io.IOException;

public interface BookHttpHandler {

    void handle(HttpExchange exchange) throws IOException, BookInvalidException;

}
