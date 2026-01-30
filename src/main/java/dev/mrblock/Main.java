package dev.mrblock;

import com.sun.net.httpserver.HttpServer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.mrblock.handler.*;
import dev.mrblock.service.BookService;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    public final static String CONNECTION_URL_TEST = System.getenv("CONNECTION_URL_TEST");
    public final static String USER_NAME_TEST = System.getenv("USER_NAME_TEST");
    public final static String PASSWORD_TEST = System.getenv("PASSWORD_TEST");

    public static void main(String[] args) throws IOException {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(CONNECTION_URL_TEST);
        config.setUsername(USER_NAME_TEST);
        config.setPassword(PASSWORD_TEST);
        config.setMaximumPoolSize(5);
        HikariDataSource dataSource = new HikariDataSource(config);

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        BookService bookService = new BookService(dataSource);

        GetBookByIdHandle getBookByIdHandle = new GetBookByIdHandle(bookService);
        RemoveBookHandle removeBookHandle = new RemoveBookHandle(bookService);
        UpdateBookHandle updateBookHandle = new UpdateBookHandle(bookService);

        GlobalBookHandle globalBookHandle = new GlobalBookHandle(
                getBookByIdHandle,
                removeBookHandle,
                updateBookHandle
        );

        CreateBookHandle createBookHandle = new CreateBookHandle(bookService);
        GetBooksByPageHandle getBooksByPageHandle = new GetBooksByPageHandle(bookService);
        BookHandle bookHandle = new BookHandle(createBookHandle, getBooksByPageHandle);

        server.createContext("/api/books/", globalBookHandle);
        server.createContext("/api/books", bookHandle);

        server.setExecutor(null);
        server.start();

        System.out.println("Server started on port 8080");
    }
}