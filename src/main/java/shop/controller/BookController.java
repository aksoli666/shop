package shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.dto.request.BookSearchParamsDto;
import shop.dto.request.CreateBookRequestDto;
import shop.dto.request.UpdateBookRequestDto;
import shop.dto.responce.BookDto;
import shop.service.BookService;

@Tag(name = "Book management", description = "Endpoints for managing books")
@RestController
@RequestMapping(value = "/api/books", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @Operation(
            summary = "Create a new book",
            description = "Create a new book"
    )
    @PreAuthorize("hasRole('ADMIN')")
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
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @Operation(
            summary = "Get all books",
            description = "Retrieve book catalog"
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public List<BookDto> getAllBooks(Pageable pageable) {
        return bookService.getAllBooks(pageable);
    }

    @Operation(
            summary = "Update a book by id",
            description = "Update a specific book"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable Long id,
                              @RequestBody @Valid UpdateBookRequestDto updateBookDto) {
        return bookService.updateBookById(id, updateBookDto);
    }

    @Operation(
            summary = "Delete a book by id",
            description = "Delete a specific book"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }

    @Operation(
            summary = "Search for books",
            description = "Search for books using a specific parameter"
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/search")
    public List<BookDto> searchBook(BookSearchParamsDto searchParamsDto, Pageable pageable) {
        return bookService.search(searchParamsDto, pageable);
    }
}
