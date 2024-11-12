package shop.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
import shop.service.impl.BookServiceImpl;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private static final int PAGE_NUMBER = 0;
    private static final int PAGE_SIZE = 20;
    private static final Long ID_1L_CORRECT = 1L;
    private static final Long INCORRECT_ID = -100L;
    private static final String CATEGORY_NAME_CORRECT = "Test Category";
    private static final String BOOK_TITLE_CORRECT = "Test Book";
    private static final String BOOK_AUTHOR_CORRECT = "Test Author";
    private static final String BOOK_ISBN_CORRECT = "Test Isbn";
    private static final BigDecimal BOOK_PRICE_CORRECT = BigDecimal.TEN;

    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder specificationBuilder;
    private Pageable pageable;

    @Test
    @DisplayName("""
            Verify createBook(...), return BookDto
            """)
    public void createBook_ValidCreateBookDto_returnBookDto() {
        CreateBookRequestDto dto = new CreateBookRequestDto();
        dto.setTitle(BOOK_TITLE_CORRECT);
        dto.setAuthor(BOOK_AUTHOR_CORRECT);
        dto.setIsbn(BOOK_ISBN_CORRECT);
        dto.setPrice(BOOK_PRICE_CORRECT);
        dto.setCategoryIds(new HashSet<>(Set.of(ID_1L_CORRECT)));

        Book book = createBookId();

        BookDto expected = createBookDto();

        when(bookMapper.toBook(dto)).thenReturn(book);
        when(categoryRepository.getReferenceById(ID_1L_CORRECT)).thenReturn(createCategory());
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toBookDto(book)).thenReturn(expected);

        BookDto actual = bookService.createBook(dto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify getBookById(...), return BookDto
            """)
    public void getBookById_ValidId_returnBookDto() {
        Book book = createBookId();

        BookDto expected = createBookDto();

        when(bookRepository.findBookById(ID_1L_CORRECT)).thenReturn(Optional.of(book));
        when(bookMapper.toBookDto(book)).thenReturn(expected);

        BookDto actual = bookService.getBookById(ID_1L_CORRECT);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify() getBookById(...), throw EntityNotFoundException
            """)
    public void getBookById_InvalidId_throwEntityNotFoundException() {
        when(bookRepository.findBookById(INCORRECT_ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> bookService.getBookById(INCORRECT_ID));
    }

    @Test
    @DisplayName("""
            Verify() getAllBooks(...), return Page<BookDto>
            """)
    public void getAllBooks_ValidPage_returnPageBookDto() {
        pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        Book book = createBookId();

        BookDto dto = createBookDto();

        Page<Book> books = new PageImpl<>(List.of(book), pageable, 1);
        Page<BookDto> expected = new PageImpl<>(List.of(dto), pageable, 1);

        when(bookRepository.findAll(pageable)).thenReturn(books);
        when(bookMapper.toBookDtoPage(books)).thenReturn(expected);

        Page<BookDto> actual = bookService.getAllBooks(pageable);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify updateBookById(...), return BookDto
            """)
    public void updateBookById_ValidId_returnBookDto() {
        UpdateBookRequestDto dto = createUpdateBookRequestDto();

        Book book = createBookId();

        BookDto expected = createBookDto();

        when(bookRepository.findById(ID_1L_CORRECT)).thenReturn(Optional.of(book));
        when(categoryRepository.getReferenceById(ID_1L_CORRECT)).thenReturn(createCategory());
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toBookDto(book)).thenReturn(expected);

        BookDto actual = bookService.updateBookById(ID_1L_CORRECT, dto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify search(...), return Page<BookDto>
            """)
    public void search_ValidSearchParams_returnPageBookDto() {
        pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        BookSearchParamsDto params = new BookSearchParamsDto(
                new String[]{BOOK_TITLE_CORRECT},
                new String[]{BOOK_AUTHOR_CORRECT},
                new String[]{BOOK_ISBN_CORRECT}
        );

        Specification<Book> build = specificationBuilder.build(params);

        Book book = createBookId();

        BookDto dto = createBookDto();

        Page<Book> books = new PageImpl<>(List.of(book), pageable, 1);
        Page<BookDto> expected = new PageImpl<>(List.of(dto), pageable, 1);

        when(bookRepository.findAll(build, pageable)).thenReturn(books);
        when(bookMapper.toBookDtoPage(books)).thenReturn(expected);

        Page<BookDto> actual = bookService.search(params, pageable);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("""
            Verify getBooksByCategoryId(...), return Page<BookDtoWithoutCategory>
            """)
    public void getBooksByCategoryId_ValidCategoryId_returnPageBookDtoWithoutCategory() {
        pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

        Book book = createBookId();

        BookDtoWithoutCategories dto = new BookDtoWithoutCategories();
        dto.setId(ID_1L_CORRECT);
        dto.setTitle(BOOK_TITLE_CORRECT);
        dto.setAuthor(BOOK_AUTHOR_CORRECT);
        dto.setIsbn(BOOK_ISBN_CORRECT);
        dto.setPrice(BOOK_PRICE_CORRECT);

        Page<Book> books = new PageImpl<>(List.of(book), pageable, 1);
        Page<BookDtoWithoutCategories> expected = new PageImpl<>(List.of(dto), pageable, 1);

        when(bookRepository.findAllByCategoryId(ID_1L_CORRECT, pageable)).thenReturn(books);
        when(bookMapper.toBookDtoWithoutCategoriesPage(books)).thenReturn(expected);

        Page<BookDtoWithoutCategories> actual = bookService.getBooksByCategoryId(ID_1L_CORRECT, pageable);

        assertEquals(expected, actual);
    }

    private Category createCategory() {
        Category category = new Category();
        category.setName(CATEGORY_NAME_CORRECT);
        return category;
    }

    private Book createBookId() {
        Category category = createCategory();

        Book book = new Book();
        book.setTitle(BOOK_TITLE_CORRECT);
        book.setAuthor(BOOK_AUTHOR_CORRECT);
        book.setIsbn(BOOK_ISBN_CORRECT);
        book.setPrice(BOOK_PRICE_CORRECT);
        book.setCategories(new HashSet<>(Set.of(category)));
        return book;
    }

    private BookDto createBookDto() {
        BookDto dto = new BookDto();
        dto.setId(ID_1L_CORRECT);
        dto.setTitle(BOOK_TITLE_CORRECT);
        dto.setAuthor(BOOK_AUTHOR_CORRECT);
        dto.setIsbn(BOOK_ISBN_CORRECT);
        dto.setPrice(BOOK_PRICE_CORRECT);
        dto.setCategoryIds(new HashSet<>(Set.of(ID_1L_CORRECT)));
        return dto;
    }

    private UpdateBookRequestDto createUpdateBookRequestDto() {
        UpdateBookRequestDto dto = new UpdateBookRequestDto();
        dto.setTitle(BOOK_TITLE_CORRECT);
        dto.setAuthor(BOOK_AUTHOR_CORRECT);
        dto.setIsbn(BOOK_ISBN_CORRECT);
        dto.setPrice(BOOK_PRICE_CORRECT);
        dto.setCategoryIds(new HashSet<>(Set.of(ID_1L_CORRECT)));
        return dto;
    }
}
