package shop.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shop.dto.request.BookSearchParamsDto;
import shop.dto.request.CreateBookRequestDto;
import shop.dto.request.UpdateBookRequestDto;
import shop.dto.responce.BookDto;
import shop.entity.Book;
import shop.exception.EntityNotFoundException;
import shop.mapper.BookMapper;
import shop.repository.book.BookRepository;
import shop.repository.book.spec.tools.impl.BookSpecificationBuilder;
import shop.service.BookService;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto createBook(CreateBookRequestDto createBookDto) {
        return bookMapper.toBookDto(
                bookRepository.save(bookMapper.toBook(createBookDto))
        );
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t get book by id: " + id));
        return bookMapper.toBookDto(book);
    }

    @Override
    public List<BookDto> getAllBooks(Pageable pageable) {
        return bookMapper.toBookDtoList(bookRepository.findAll(pageable).getContent());
    }

    @Override
    public BookDto updateBookById(Long id, UpdateBookRequestDto updateBookDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found. Id: " + id));
        bookMapper.updateBookFromDto(updateBookDto, book);
        return bookMapper.toBookDto(bookRepository.save(book));
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> search(BookSearchParamsDto searchParamsDto, Pageable pageable) {
        return bookRepository.findAll(bookSpecificationBuilder.build(searchParamsDto), pageable)
                .stream()
                .map(bookMapper::toBookDto)
                .toList();
    }
}
