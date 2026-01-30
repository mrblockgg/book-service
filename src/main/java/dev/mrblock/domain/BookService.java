package dev.mrblock.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    @Nullable
    public Book findById(long id) throws Exception {
        return bookRepository.findById(id);
    }

    public List<Book> findPageable(int page) throws Exception {
        return bookRepository.findPageable(10, page);
    }

    public void newBook(Book book) throws Exception {
        bookRepository.insert(book);
    }

    public void update(long id, Book book) throws Exception {
        bookRepository.update(id, book.getTitle(), book.getAuthor(), book.getPublicationYear(), book.getGenre());
    }

    public void removeById(long id) throws Exception {
        bookRepository.removeById(id);
    }
}