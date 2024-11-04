package shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import shop.dto.request.book.BookSearchParamsDto;
import shop.dto.request.book.CreateBookRequestDto;
import shop.dto.request.book.UpdateBookRequestDto;
import shop.dto.responce.book.BookDto;
import shop.dto.responce.book.BookDtoWithoutCategories;
import shop.service.BookService;

@Tag(name = "Book management", description = "Endpoints for managing books")
@RestController
@RequestMapping(value = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @Operation(
            summary = "Create a new book",
            description = "Create a new book"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto createBookDto) {
        return bookService.createBook(createBookDto);
    }

    @Operation(
            summary = "Get a book by id",
            description = "Retrieve a book details by id"
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable @Positive Long id) {
        return bookService.getBookById(id);
    }

    @Operation(
            summary = "Get all books",
            description = "Retrieve book catalog"
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public Page<BookDto> getAllBooks(Pageable pageable) {
        return bookService.getAllBooks(pageable);
    }

    @Operation(
            summary = "Update a book by id",
            description = "Update a specific book"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable @Positive Long id,
                              @RequestBody @Valid UpdateBookRequestDto updateBookDto) {
        return bookService.updateBookById(id, updateBookDto);
    }

    @Operation(
            summary = "Delete a book by id",
            description = "Delete a specific book"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable @Positive Long id) {
        bookService.deleteBookById(id);
    }

    @Operation(
            summary = "Search for books",
            description = "Search for books using a specific parameter"
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/search")
    public Page<BookDto> searchBook(BookSearchParamsDto searchParamsDto, Pageable pageable) {
        return bookService.search(searchParamsDto, pageable);
    }

    @Operation(
            summary = "Get all books by category id",
            description = "Retrieve book catalog by category id"
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{categoryId}/books")
    public Page<BookDtoWithoutCategories> getBooksByCategoryId(
            @PathVariable @Positive Long categoryId, Pageable pageable) {
        return bookService.getBooksByCategoryId(categoryId, pageable);
    }
}
