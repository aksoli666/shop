package shop.service;

import java.util.List;
import shop.dto.request.CreateBookRequestDto;
import shop.dto.request.UpdateBookRequestDto;
import shop.dto.responce.BookDto;

public interface BookService {
    BookDto createBook(CreateBookRequestDto createBookDto);

    BookDto getBookById(Long id);

    List<BookDto> getAllBooks();

    BookDto updateBookById(Long id, UpdateBookRequestDto updateBookDto);

    void deleteBookById(Long id);
}
