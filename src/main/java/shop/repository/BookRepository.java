package shop.repository;

import java.util.List;
import shop.entity.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();
}
