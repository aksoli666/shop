package shop.service;

import java.util.List;
import shop.dto.request.CreateBookDto;
import shop.dto.responce.BookDto;

public interface BookService {
    BookDto createBook(CreateBookDto createBookDto);

    BookDto getBookById(Long id);

    List<BookDto> getAllBooks();
}
