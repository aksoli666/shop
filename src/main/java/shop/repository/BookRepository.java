package shop.repository;

import java.util.List;
import java.util.Optional;
import shop.entity.Book;

public interface BookRepository {
    Book createBook(Book book);

    Optional<Book> findBookById(Long id);

    List<Book> findAllBooks();
}
