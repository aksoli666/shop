package shop.service.impl;

import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import shop.dto.request.book.BookSearchParamsDto;
import shop.dto.request.book.CreateBookRequestDto;
import shop.dto.request.book.UpdateBookRequestDto;
import shop.dto.responce.book.BookDto;
import shop.dto.responce.book.BookDtoWithoutCategories;
import shop.entity.Book;
import shop.entity.Category;
import shop.exception.EntityNotFoundException;
import shop.mapper.BookMapper;
import shop.repository.BookRepository;
import shop.repository.CategoryRepository;
import shop.repository.spec.tools.book.spec.tools.impl.BookSpecificationBuilder;
import shop.service.BookService;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto createBook(CreateBookRequestDto createBookDto) {
        Book book = bookMapper.toBook(createBookDto);
        fetchCategoriesAndSetToBook(createBookDto.getCategoryIds(), book);
        return bookMapper.toBookDto(bookRepository.save(book));
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findBookById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t get book by id: " + id));
        return bookMapper.toBookDto(book);
    }

    @Override
    public Page<BookDto> getAllBooks(Pageable pageable) {
        return bookMapper.toBookDtoPage(bookRepository.findAll(pageable));
    }

    @Override
    public BookDto updateBookById(Long id, UpdateBookRequestDto updateBookDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found. Id: " + id));
        fetchCategoriesAndSetToBook(updateBookDto.getCategoryIds(), book);
        bookMapper.updateBookFromDto(updateBookDto, book);
        return bookMapper.toBookDto(bookRepository.save(book));
    }

    @Override
    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public Page<BookDto> search(BookSearchParamsDto searchParamsDto, Pageable pageable) {
        return bookMapper.toBookDtoPage(
                bookRepository.findAll(
                        bookSpecificationBuilder.build(searchParamsDto), pageable)
        );
    }

    @Override
    public Page<BookDtoWithoutCategories> getBooksByCategoryId(Long categoryId, Pageable pageable) {
        return bookMapper.toBookDtoWithoutCategoriesPage(
                bookRepository.findAllByCategoryId(categoryId, pageable));
    }

    private void fetchCategoriesAndSetToBook(Set<Long> categoryIds, Book book) {
        Set<Category> categories = categoryIds.stream()
                .map(categoryRepository::getReferenceById)
                .collect(Collectors.toSet());
        book.setCategories(categories);
    }
}
