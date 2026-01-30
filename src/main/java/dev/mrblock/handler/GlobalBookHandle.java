package dev.mrblock.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GlobalBookHandle implements HttpHandler {

    GetBookByIdHandle getBookByIdHandle;
    RemoveBookHandle removeBookHandle;
    UpdateBookHandle updateBookHandle;

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String requestMethod = exchange.getRequestMethod();

            if ("GET".equals(requestMethod)) getBookByIdHandle.handle(exchange);
            else if ("DELETE".equals(requestMethod)) removeBookHandle.handle(exchange);
            else if ("PATCH".equals(requestMethod)) updateBookHandle.handle(exchange);
        } catch (Exception ignore) {}
    }
}