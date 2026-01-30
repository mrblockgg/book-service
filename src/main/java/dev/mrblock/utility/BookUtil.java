package dev.mrblock.utility;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.sun.net.httpserver.HttpExchange;
import dev.mrblock.domain.Book;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public class BookUtil {

    private static final JsonMapper JSON_MAPPER = new JsonMapper();

    public static Book readBookFromExchangeBody(HttpExchange exchange) throws IOException {
        InputStream requestBodyIS = exchange.getRequestBody();
        String body = new String(requestBodyIS.readAllBytes());

        return JSON_MAPPER.readValue(body, Book.class);
    }

    public static void writeBookToExchangeResponseBody(HttpExchange exchange, Book book) throws IOException {
        String response = JSON_MAPPER.writeValueAsString(book);
        ExchangeUtil.writeJsonBodyToExchange(exchange, response);
    }

    public static void writeBooksToExchangeResponseBody(HttpExchange exchange, Collection<Book> books) throws IOException {
        String response = JSON_MAPPER.writeValueAsString(books);
        ExchangeUtil.writeJsonBodyToExchange(exchange, response);
    }

}
