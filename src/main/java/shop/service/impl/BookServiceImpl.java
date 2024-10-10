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
        Book bookFromRepository = bookRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can`t get book by id: " + id)
                );
        return bookMapper.toBookDto(bookFromRepository);
    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookMapper.toBookDtoList(bookRepository.findAll());
    }

    @Override
    public BookDto updateBookById(Long id, UpdateBookDto updateBookDto) {
        if (bookRepository.updateBookById(id,
                updateBookDto.getTitle(),
                updateBookDto.getAuthor(),
                updateBookDto.getIsbn(),
                updateBookDto.getPrice(),
                updateBookDto.getDescription(),
                updateBookDto.getCoverImage()) > -1) {
            return getBookById(id);
        }
        throw new EntityNotFoundException("Can`t update book by id: " + id);
    }

    @Override
    public String deleteBookById(Long id) {
        bookRepository.deleteById(id);
        return "Book deleted";
    }
}
