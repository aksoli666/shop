package shop.service;

import java.util.List;
import org.springframework.data.domain.Pageable;
import shop.dto.request.book.BookSearchParamsDto;
import shop.dto.request.book.CreateBookRequestDto;
import shop.dto.request.book.UpdateBookRequestDto;
import shop.dto.responce.book.BookDto;
import shop.dto.responce.book.BookDtoWithoutCategories;

public interface BookService {
    BookDto createBook(CreateBookRequestDto createBookDto);

    BookDto getBookById(Long id);

    List<BookDto> getAllBooks(Pageable pageable);

    BookDto updateBookById(Long id, UpdateBookRequestDto updateBookDto);

    void deleteBookById(Long id);

    List<BookDto> search(BookSearchParamsDto searchParamsDto, Pageable pageable);

    List<BookDtoWithoutCategories> getBooksByCategoryId(Long categoryId);
}
