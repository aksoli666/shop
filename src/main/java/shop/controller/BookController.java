package shop.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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

@RestController
@RequestMapping(value = "/api/books", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping
    public BookDto createBook(@RequestBody @Valid CreateBookRequestDto createBookDto) {
        return bookService.createBook(createBookDto);
    }

    @GetMapping("/{id}")
    public BookDto getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    @GetMapping
    public List<BookDto> getAllBooks() {
        return bookService.getAllBooks();
    }

    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable Long id,
                              @RequestBody @Valid UpdateBookRequestDto updateBookDto) {
        return bookService.updateBookById(id, updateBookDto);
    }

    @DeleteMapping("/{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }

    @GetMapping("/search")
    public List<BookDto> searchBook(BookSearchParamsDto searchParamsDto) {
        return bookService.search(searchParamsDto);
    }
}
