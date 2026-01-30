package dev.mrblock.handler;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.mrblock.entity.Book;
import dev.mrblock.service.BookService;
import dev.mrblock.utility.ParseQuery;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GetBooksByPageHandle implements HttpHandler {

    BookService bookService;
    JsonMapper jsonMapper = new JsonMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            URI uri = exchange.getRequestURI();
            Map<String, String> params = ParseQuery.parseQueryParameters(uri.getQuery());

            int page = Integer.parseInt(params.get("page"));

            try {
                List<Book> books = bookService.getBooks(page);

                String response = jsonMapper.writeValueAsString(books);
                byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.getResponseHeaders().set("Content-Length", String.valueOf(responseBytes.length));

                exchange.sendResponseHeaders(200, responseBytes.length);

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(responseBytes);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            exchange.sendResponseHeaders(200, 0);
            exchange.close();
        }
    }
}