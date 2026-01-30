package dev.mrblock.utility;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ExchangeUtil {

    public static void sendCodeResponse(HttpExchange exchange, int code) throws IOException {
        exchange.sendResponseHeaders(code, 0);
        exchange.close();
    }

    public static void writeJsonBodyToExchange(HttpExchange exchange, String body) throws IOException {
        byte[] bodyBytes = body.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().set("Content-Length", String.valueOf(bodyBytes.length));

        exchange.sendResponseHeaders(200, bodyBytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bodyBytes);
        }
    }

}
