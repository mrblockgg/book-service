package dev.mrblock.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookHandle implements HttpHandler {

    CreateBookHandle createBookHandle;
    GetBooksByPageHandle getBooksByPageHandle;

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String requestMethod = exchange.getRequestMethod();

            if ("POST".equals(requestMethod)) createBookHandle.handle(exchange);
            else if ("GET".equals(requestMethod)) getBooksByPageHandle.handle(exchange);
        } catch (Exception ignore) {
        }
    }
}