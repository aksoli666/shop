package shop.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import shop.dto.request.BookSearchParamsDto;
import shop.dto.request.CreateBookRequestDto;
import shop.dto.request.UpdateBookRequestDto;
import shop.dto.responce.BookDto;

public interface BookService {
    BookDto createBook(CreateBookRequestDto createBookDto);

    BookDto getBookById(Long id);

    List<BookDto> getAllBooks(Pageable pageable);

    BookDto updateBookById(Long id, UpdateBookRequestDto updateBookDto);

    void deleteBookById(Long id);

    List<BookDto> search(BookSearchParamsDto searchParamsDto, Pageable pageable);
}
