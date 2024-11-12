package shop.controller;

import static org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
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
import shop.dto.request.category.UpdateCategoryRequestDto;
import shop.dto.responce.category.CategoryDto;

@Sql(
        scripts = {"classpath:database/shop/category/add-categories.sql"},
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
@Sql(
        scripts = {"classpath:database/shop/category/delete-categories.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    private static final String ADD_CATEGORY_FOR_DELETE_SQL =
            "classpath:database/shop/category/add-category-for-delete.sql";
    private static final String UPDATE_CATEGORY_SQL =
            "classpath:database/shop/category/update-category.sql";
    private static final String ADD_CATEGORY_FOR_CREATE_SQL =
            "classpath:database/shop/category/add-category-for-create.sql";
    private static final String URL_WITHOUT_ID = "/categories";
    private static final String URL_WITH_ID = "/categories/{id}";
    private static final Long CATEGORY_ID = 5L;
    private static final Long DELETED_CATEGORY_ID = 8L;
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
            scripts = {ADD_CATEGORY_FOR_CREATE_SQL},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Create a new category")
    public void createCategory_ValidRequestDto_Success() throws Exception {
        CategoryDto expected = new CategoryDto(
                10L,
                "Y Category",
                "Y Description");

        String json = objectMapper.writeValueAsString(expected);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post(URL_WITHOUT_ID)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class);

        reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Retrieve category catalog")
    public void getAll_categoryCatalog_ReturnPageDtos() throws Exception {
        CategoryDto firstDto = new CategoryDto(
                6L,
                "Q Category",
                "Q Description");
        CategoryDto secondDto = new CategoryDto(
                7L,
                "W Category",
                "W Description");

        Page<CategoryDto> expected = new PageImpl<>(List.of(firstDto, secondDto), pageable, 2);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get(URL_WITHOUT_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String actual = result.getResponse().getContentAsString();

        reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "user")
    @Test
    @DisplayName("Retrieve a category details by id")
    public void getCategoryById_GivenDto_Success() throws Exception {
        CategoryDto expected = new CategoryDto(
                6L,
                "E Category",
                "E Description for Category E");

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get(URL_WITH_ID, 6L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class
        );

        reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = UPDATE_CATEGORY_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Update a specific category")
    public void updateCategory_GivenDto_Success() throws Exception {
        UpdateCategoryRequestDto dto = new UpdateCategoryRequestDto();
        dto.setName("E Category Upd");
        dto.setDescription("E Description for Category E");

        CategoryDto expected = new CategoryDto(
                CATEGORY_ID,
                dto.getName(),
                dto.getDescription()
        );

        String json = objectMapper.writeValueAsString(expected);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post(URL_WITH_ID, CATEGORY_ID)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        CategoryDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryDto.class
        );

        reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Delete a category book")
    @Sql(
            scripts = ADD_CATEGORY_FOR_DELETE_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    public void deleteCategoryBook_GivenDto_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_WITH_ID, DELETED_CATEGORY_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
