package dev.mrblock.infrastructure.http.handler;

import com.sun.net.httpserver.HttpExchange;
import dev.mrblock.domain.Book;
import dev.mrblock.domain.BookService;
import dev.mrblock.infrastructure.http.BookHttpHandler;
import dev.mrblock.utility.BookUtil;
import dev.mrblock.utility.HttpQueryUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class GetBooksByPageHandler implements BookHttpHandler {

    private final BookService bookService;

    @SneakyThrows
    @Override
    public void handle(HttpExchange exchange) {
        URI uri = exchange.getRequestURI();
        Map<String, String> params = HttpQueryUtil.parseQueryParameters(uri.getQuery());
        int page = Integer.parseInt(params.get("page"));

        List<Book> books = bookService.findPageable(page);
        BookUtil.writeBooksToExchangeResponseBody(exchange, books);
    }
}