package dev.mrblock.infrastructure.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import dev.mrblock.utility.ExchangeUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpRouter implements HttpHandler {

    private final Map<String, HttpHandler> typedHandlers = new HashMap<>();

    public void put(String httpMethod, HttpHandler handler) {
        typedHandlers.put(httpMethod, handler);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        HttpHandler handler = typedHandlers.get(requestMethod);

        if (handler == null) {
            exchange.sendResponseHeaders(405, 0);
            exchange.close();
        } else {
            handle(exchange, handler);
        }
    }

    private void handle(HttpExchange exchange, HttpHandler handler) throws IOException {
        try {
            handler.handle(exchange);
        } catch (Exception e) {
            log.error(e.getMessage());
            ExchangeUtil.sendCodeResponse(exchange, 500);
        }
    }
}
