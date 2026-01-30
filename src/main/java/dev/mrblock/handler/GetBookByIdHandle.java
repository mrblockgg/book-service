package dev.mrblock.handler;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.mrblock.entity.Book;
import dev.mrblock.service.BookService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GetBookByIdHandle implements HttpHandler {

    BookService bookService;
    JsonMapper jsonMapper = new JsonMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();

            String[] parts = path.split("/");
            if (parts.length >= 4) {
                String id = parts[3];

                Book bookById = bookService.getBookById(Long.parseLong(id));

                if (bookById == null) {
                    exchange.sendResponseHeaders(404, 0);

                    exchange.close();
                    return;
                }

                String response = jsonMapper.writeValueAsString(bookById);
                byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.getResponseHeaders().set("Content-Length", String.valueOf(responseBytes.length));

                exchange.sendResponseHeaders(200, responseBytes.length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(responseBytes);
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            exchange.sendResponseHeaders(500, 0);
            exchange.close();
        }
    }
}