package shop.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    public List<BookDto> getAllBooks(Pageable pageable) {
        Page<Book> booksPage = bookRepository.findAll(pageable);
        return bookMapper.toBookDtoList(booksPage.getContent());
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
    public List<BookDto> search(BookSearchParamsDto searchParamsDto, Pageable pageable) {
        Page<Book> bookPage = bookRepository.findAll(
                bookSpecificationBuilder.build(searchParamsDto), pageable);
        return bookPage.getContent().stream()
                .map(bookMapper::toBookDto)
                .toList();
    }

    @Override
    public List<BookDtoWithoutCategories> getBooksByCategoryId(Long categoryId) {
        return bookMapper.toBookDtoWithoutCategoriesList(
                bookRepository.findAllByCategoryId(categoryId));
    }

    private void fetchCategoriesAndSetToBook(Set<Long> categoryIds, Book book) {
        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(categoryIds));
        book.setCategories(categories);
    }
}
