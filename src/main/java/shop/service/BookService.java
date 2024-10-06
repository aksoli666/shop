package shop.service;

import java.util.List;
import shop.entity.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
