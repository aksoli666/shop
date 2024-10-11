package shop.service;

import java.util.List;
import shop.dto.request.CreateBookDto;
import shop.dto.request.UpdateBookDto;
import shop.dto.responce.BookDto;

public interface BookService {
    BookDto createBook(CreateBookDto createBookDto);

    BookDto getBookById(Long id);

    List<BookDto> getAllBooks();

    BookDto updateBookById(Long id, UpdateBookDto updateBookDto);

    void deleteBookById(Long id);
}
