package shop.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.dto.request.CreateBookDto;
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
                bookRepository.createBook(
                        bookMapper.toBook(createBookDto)
                )
        );
    }

    @Override
    public BookDto getBookById(Long id) {
        Book bookFromRepository = bookRepository.findBookById(id).orElseThrow(
                        () -> new EntityNotFoundException("Can`t get book by id: " + id));
        return bookMapper.toBookDto(bookFromRepository);
    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookRepository.findAllBooks().stream()
                .map(bookMapper::toBookDto)
                .toList();
    }
}
