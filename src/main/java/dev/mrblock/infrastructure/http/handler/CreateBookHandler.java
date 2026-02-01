package dev.mrblock.infrastructure.http.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.mrblock.data.mapper.BookMapper;
import dev.mrblock.domain.Book;
import dev.mrblock.domain.BookService;
import dev.mrblock.utility.BookUtil;
import dev.mrblock.utility.CheckUtil;
import dev.mrblock.utility.ExchangeUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CreateBookHandler implements HttpHandler {

    private final BookService bookService;

    @SneakyThrows
    @Override
    public void handle(HttpExchange exchange) {
        Book book = BookUtil.readBookFromExchangeBody(exchange);
        CheckUtil.check(book, exchange);

        bookService.newBook(book);
        ExchangeUtil.sendCodeResponse(exchange, 200);
    }


}