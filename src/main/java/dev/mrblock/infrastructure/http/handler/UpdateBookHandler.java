package dev.mrblock.infrastructure.http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.mrblock.domain.Book;
import dev.mrblock.domain.BookService;
import dev.mrblock.utility.BookUtil;
import dev.mrblock.utility.ExchangeUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UpdateBookHandler implements HttpHandler {

    private final BookService bookService;

    @SneakyThrows
    @Override
    public void handle(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String[] parts = path.split("/");

        if (parts.length != 4) {
            ExchangeUtil.sendCodeResponse(exchange, 400);
            return;
        }

        long bookId = Long.parseLong(parts[3]);
        Book book = BookUtil.readBookFromExchangeBody(exchange);

        bookService.update(bookId, book);

        ExchangeUtil.sendCodeResponse(exchange, 200);
    }
}
