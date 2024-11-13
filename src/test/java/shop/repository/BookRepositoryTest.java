package shop.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import shop.entity.Book;
import shop.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    private static final Long ID_1L = 1L;
    private static final String CATEGORY_NAME = "Test Category";
    private static final String BOOK_TITLE = "Test Book";
    private static final String BOOK_AUTHOR = "Test Author";

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("""
            Verify findAllByCategoryId(...), return Page<Book>
            """)
    void findAllByCategoryId_correctId_returnPageDto() {
        Category category = new Category();
        category.setName(CATEGORY_NAME);
        categoryRepository.save(category);

        Set<Category> categories = new HashSet<>();
        categories.add(category);

        Book book = new Book();
        book.setId(ID_1L);
        book.setTitle(BOOK_TITLE);
        book.setAuthor(BOOK_AUTHOR);
        book.setIsbn("test_isbn");
        book.setPrice(BigDecimal.valueOf(100));
        book.setCategories(categories);
        bookRepository.save(book);

        Page<Book> books = bookRepository
                .findAllByCategoryId(category.getId(), PageRequest.of(0, 20));

        assertNotNull(books);
        assertEquals(1, books.getTotalElements());
        assertEquals(BOOK_TITLE, books.getContent().get(0).getTitle());
        assertEquals(BOOK_AUTHOR, books.getContent().get(0).getAuthor());
    }
}
