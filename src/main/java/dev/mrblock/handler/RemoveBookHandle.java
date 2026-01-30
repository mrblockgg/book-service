package dev.mrblock.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.mrblock.entity.Book;
import dev.mrblock.service.BookService;
import dev.mrblock.utility.ParseQuery;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RemoveBookHandle implements HttpHandler {

    BookService bookService;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if ("DELETE".equals(exchange.getRequestMethod())) {
                String path = exchange.getRequestURI().getPath();

                String[] parts = path.split("/");
                if (parts.length >= 4) {
                    String id = parts[3];
                    bookService.removeBook(Long.parseLong(id));

                    exchange.sendResponseHeaders(200, 0);
                    exchange.close();
                }

            }
        } catch (Exception e) {
            log.error(e.getMessage());
            exchange.sendResponseHeaders(500, 0);
            exchange.close();
        }
    }
}