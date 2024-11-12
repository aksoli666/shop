package shop.controller;

import static org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import shop.dto.request.book.CreateBookRequestDto;
import shop.dto.request.book.UpdateBookRequestDto;
import shop.dto.responce.book.BookDto;
import shop.dto.responce.book.BookDtoWithoutCategories;

@Sql(
        scripts = {"classpath:database/shop/book/add-books.sql",
                "classpath:database/shop/category/add-category.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
@Sql(
        scripts = "classpath:database/shop/book/delete-books.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    private static final String ADD_BOOK_SQL =
            "classpath:database/shop/book/add-book.sql";
    private static final String DELETE_BOOK_BY_TITLE_SQL =
            "classpath:database/shop/book/delete-books.sql";
    private static final String ADD_BOOK_FOR_CREATE_SQL =
            "classpath:database/shop/book/add-book-for-create.sql";
    private static final String UPDATE_BOOK_SQL =
            "classpath:database/shop/book/update-book.sql";
    private static final String DELETE_UPDATED_BOOK_SQL =
            "classpath:database/shop/book/delete-updated-book.sql";
    private static final String ADD_BOOK_FOR_DELETE_BY_ID_SQL =
            "classpath:database/shop/book/add-book-for-delete-by-id.sql";
    private static final String URL_WITHOUT_ID = "/books";
    private static final String URL_WITH_ID = "/books/{id}";
    private static final String URL_SEARCH = "/books/search";
    private static final String URL_WITH_CATEGORY_ID = "/books/{categoryId}/books";
    private static final Long BOOK_ID = 8L;
    private static final Long DELETE_BOOK_ID = 12L;
    private static final Long ID_5L_CATEGORY_CORRECT = 5L;
    private static final BigDecimal BOOK_PRICE = BigDecimal.valueOf(100);
    private static final Set<Long> BOOK_CATEGORY_IDS = Set.of(ID_5L_CATEGORY_CORRECT);
    private static final Pageable pageable = PageRequest.of(0, 20);

    private static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext webApplicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
        }
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = ADD_BOOK_FOR_CREATE_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Create a new book")
    public void createBook_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto dto = new CreateBookRequestDto();
        dto.setTitle("U Title");
        dto.setAuthor("U Author");
        dto.setIsbn("u_isbn");
        dto.setPrice(BOOK_PRICE);
        dto.setDescription("U Description for U book");
        dto.setCoverImage("u_cover_image");
        dto.setCategoryIds(BOOK_CATEGORY_IDS);

        BookDto expected = new BookDto();
        expected.setTitle(dto.getTitle());
        expected.setAuthor(dto.getAuthor());
        expected.setIsbn(dto.getIsbn());
        expected.setPrice(dto.getPrice());
        expected.setDescription(dto.getDescription());
        expected.setCoverImage(dto.getCoverImage());
        expected.setCategoryIds(dto.getCategoryIds());

        String json = objectMapper.writeValueAsString(expected);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post(URL_WITHOUT_ID)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "user")
    @Test
    @Sql(
            scripts = ADD_BOOK_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Retrieve a book details by id")
    public void getBookById_GivenDto_Success() throws Exception {
        BookDto expected = new BookDto();
        expected.setTitle("G Title");
        expected.setAuthor("G Author");
        expected.setIsbn("g_isbn");
        expected.setPrice(BOOK_PRICE);
        expected.setDescription("G Description for G book");
        expected.setCoverImage("g_cover_image");
        expected.setCategoryIds(BOOK_CATEGORY_IDS);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get(URL_WITH_ID, BOOK_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class
        );

        reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Retrieve book catalog")
    public void getAllBooks_GivenBooksInCatalog_ReturnPageDtos() throws Exception {
        BookDto firstDto = new BookDto();
        firstDto.setId(6L);
        firstDto.setTitle("E Title");
        firstDto.setAuthor("E Author");
        firstDto.setIsbn("e_isbn");
        firstDto.setPrice(BOOK_PRICE);
        firstDto.setDescription("E Description for E Book");
        firstDto.setCoverImage("e_cover_image");
        firstDto.setCategoryIds(BOOK_CATEGORY_IDS);
        BookDto secondDto = new BookDto();
        secondDto.setId(7L);
        secondDto.setTitle("F Title");
        secondDto.setAuthor("F Author");
        secondDto.setIsbn("f_isbn");
        secondDto.setPrice(BOOK_PRICE);
        secondDto.setDescription("F Description for F Book");
        secondDto.setCoverImage("f_cover_image");
        secondDto.setCategoryIds(BOOK_CATEGORY_IDS);

        Page<BookDto> expected = new PageImpl<>(List.of(firstDto, secondDto), pageable, 2);

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.get(URL_WITHOUT_ID)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String actual = result.getResponse().getContentAsString();

        reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = ADD_BOOK_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = UPDATE_BOOK_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_UPDATED_BOOK_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Update a specific book")
    public void updateBook_ValidRequestDto_Success() throws Exception {
        UpdateBookRequestDto dto = new UpdateBookRequestDto();
        dto.setTitle("H Title Upd");
        dto.setAuthor("H Author");
        dto.setIsbn("h_isbn");
        dto.setPrice(BOOK_PRICE);
        dto.setDescription("H Description for H book");
        dto.setCoverImage("h_cover_image");
        dto.setCategoryIds(BOOK_CATEGORY_IDS);

        BookDto expected = new BookDto();
        expected.setTitle(dto.getTitle());
        expected.setAuthor(dto.getAuthor());
        expected.setIsbn(dto.getIsbn());
        expected.setPrice(dto.getPrice());
        expected.setDescription(dto.getDescription());
        expected.setCoverImage(dto.getCoverImage());
        expected.setCategoryIds(dto.getCategoryIds());

        String json = objectMapper.writeValueAsString(expected);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.put(URL_WITH_ID, BOOK_ID)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete a specific book")
    @Sql(
            scripts = ADD_BOOK_FOR_DELETE_BY_ID_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    public void deleteBookById_GivenDto_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_WITH_ID, DELETE_BOOK_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Search for books using a specific parameter")
    public void searchBook_GivenDto_Success() throws Exception {
        BookDto dto = new BookDto();
        dto.setId(6L);
        dto.setTitle("E Title");
        dto.setAuthor("E Author");
        dto.setIsbn("e_isbn");
        dto.setPrice(BOOK_PRICE);
        dto.setDescription("E Description for E Book");
        dto.setCoverImage("e_cover_image");
        dto.setCategoryIds(BOOK_CATEGORY_IDS);

        Page<BookDto> expected = new PageImpl<>(List.of(dto), pageable, 1);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get(URL_SEARCH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String actual = result.getResponse().getContentAsString();

        reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Retrieve book catalog by category id")
    public void getBooksByCategoryId_GivenDto_Success() throws Exception {
        BookDtoWithoutCategories dto = new BookDtoWithoutCategories();
        dto.setId(6L);
        dto.setTitle("E Title");
        dto.setAuthor("E Author");
        dto.setIsbn("e_isbn");
        dto.setPrice(BOOK_PRICE);
        dto.setDescription("E Description for E Book");
        dto.setCoverImage("e_cover_image");

        Page<BookDtoWithoutCategories> expected = new PageImpl<>(List.of(dto), pageable, 1);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get(URL_WITH_CATEGORY_ID, ID_5L_CATEGORY_CORRECT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String actual = result.getResponse().getContentAsString();

        reflectionEquals(expected, actual);
    }
}
