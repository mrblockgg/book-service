package dev.mrblock.domain;

import java.util.List;

public interface BookRepository {

    Book findById(long id) throws Exception;

    List<Book> findPageable(long pageSize, long page) throws Exception;

    void insert(Book book) throws Exception;

    void update(long id, String title, String author, Integer publicationYear, String genre) throws Exception;

    void removeById(long id) throws Exception;

}
