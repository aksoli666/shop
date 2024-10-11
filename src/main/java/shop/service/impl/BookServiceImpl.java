package shop.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.dto.request.CreateBookDto;
import shop.dto.request.UpdateBookDto;
import shop.dto.responce.BookDto;
import shop.entity.Book;
import shop.exception.EntityNotFoundException;
import shop.mapper.BookMapper;
import shop.repository.BookRepository;
import shop.service.BookService;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto createBook(CreateBookDto createBookDto) {
        return bookMapper.toBookDto(
                bookRepository.save(bookMapper.toBook(createBookDto))
        );
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookMapper.toBookDto(bookRepository.findById(id).get());
    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookMapper.toBookDtoList(bookRepository.findAll());
    }

    @Override
    public BookDto updateBookById(Long id, UpdateBookDto updateBookDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found. Id: " + id));
        return bookMapper.toBookDto(
                bookRepository.save(
                        bookMapper.updateBookFromDto(updateBookDto, book)
                )
        );
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }
}
