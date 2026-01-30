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
import java.io.InputStream;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UpdateBookHandle implements HttpHandler {

    BookService bookService;
    JsonMapper jsonMapper = new JsonMapper();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();

            String[] parts = path.split("/");
            if (parts.length >= 4) {
                long id = Long.parseLong(parts[3]);

                InputStream requestBodyIS = exchange.getRequestBody();
                String body = new String(requestBodyIS.readAllBytes());

                Book book = jsonMapper.readValue(body, Book.class);

                bookService.updateBook(book.getTitle(), book.getAuthor(), book.getPublicationYear(), book.getGenre(), id);

                exchange.sendResponseHeaders(200, 0);
                exchange.close();

            }

        } catch (Exception e) {
            log.error(e.getMessage());
            exchange.sendResponseHeaders(500, 0);
            exchange.close();
        }
    }
}
