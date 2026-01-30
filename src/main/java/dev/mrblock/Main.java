package dev.mrblock;

import com.sun.net.httpserver.HttpServer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.mrblock.infrastructure.http.handler.*;
import dev.mrblock.infrastructure.http.HttpRouter;
import dev.mrblock.data.repository.PostgresBookRepository;
import dev.mrblock.domain.BookService;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    public final static String CONNECTION_URL_TEST = System.getenv("CONNECTION_URL_TEST");
    public final static String USER_NAME_TEST = System.getenv("USER_NAME_TEST");
    public final static String PASSWORD_TEST = System.getenv("PASSWORD_TEST");

    public static void main(String[] args) throws IOException {
        HikariDataSource dataSource = createHikariDatasource();
        PostgresBookRepository bookRepository = new PostgresBookRepository(dataSource);
        BookService bookService = new BookService(bookRepository);

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

        setupHttpServer(httpServer, bookService);

        httpServer.start();
        System.out.println("Server started on port 8080");
    }

    private static HikariDataSource createHikariDatasource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(CONNECTION_URL_TEST);
        config.setUsername(USER_NAME_TEST);
        config.setPassword(PASSWORD_TEST);
        config.setMaximumPoolSize(5);
        return new HikariDataSource(config);
    }

    private static void setupHttpServer(HttpServer server, BookService bookService) {
        HttpRouter mainRouter = createHttpRouterForMainBookContext(bookService);
        HttpRouter subRouter = createHttpRouterForSubBookContext(bookService);

        server.createContext("/api/books/", subRouter);
        server.createContext("/api/books", mainRouter);

        server.setExecutor(null);
    }

    private static HttpRouter createHttpRouterForSubBookContext(BookService bookService) {
        HttpRouter httpRouter = new HttpRouter();
        httpRouter.put("GET", new GetBookByIdHandler(bookService));
        httpRouter.put("UPDATE", new RemoveBookHandler(bookService));
        httpRouter.put("REMOVE", new UpdateBookHandler(bookService));

        return httpRouter;
    }

    private static HttpRouter createHttpRouterForMainBookContext(BookService bookService) {
        HttpRouter httpRouter = new HttpRouter();
        httpRouter.put("POST", new CreateBookHandler(bookService));
        httpRouter.put("GET", new GetBooksByPageHandler(bookService));
        return httpRouter;
    }
}