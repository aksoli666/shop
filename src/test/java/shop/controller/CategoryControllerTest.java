package shop.controller;

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
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import shop.dto.request.category.CreateCategoryRequestDto;
import shop.dto.request.category.UpdateCategoryRequestDto;
import shop.dto.responce.category.CategoryDto;

import static org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    private static final String ADD_CATEGORIES_SQL =
            "database/shop/category/add-categories.sql";
    private static final String DELETE_CATEGORIES_SQL =
            "database/shop/category/delete-categories.sql";
    private static final String ADD_CATEGORY_SQL =
            "classpath:database/shop/category/add-category.sql";
    private static final String DELETE_CATEGORY_BY_NAME_SQL =
            "classpath:database/shop/category/delete-category-by-name.sql";
    private static final String ADD_CATEGORY_GET_BY_ID_SQL =
            "classpath:database/shop/category/add-category-for-get-by-id.sql";
    private static final String DELETE_CATEGORY_GET_BY_ID_SQL =
            "classpath:database/shop/category/delete-category-for-get-by-id.sql";
    private static final String ADD_CATEGORY_FOR_UPDATE_SQL =
            "classpath:database/shop/category/add-category-for-update.sql";
    private static final String UPDATE_CATEGORY_SQL =
            "classpath:database/shop/category/update-category.sql";
    private static final String DELETE_CATEGORY_FOR_UPDATE_SQL =
            "classpath:database/shop/category/delete-category-for-update.sql";
    private static final String ADD_CATEGORY_FOR_DELETE_BY_ID_SQL =
            "classpath:database/shop/category/add-category-for-delete-by-id.sql";
    private static final String DELETE_CATEGORY_FOR_DELETE_BY_ID_SQL =
            "classpath:database/shop/category/delete-category-for-delete-by-id.sql";
    private static final String URL_WITHOUT_ID = "/categories";
    private static final String URL_WITH_ID = "/categories/{id}";
    private static final Long CATEGORY_ID_FOR_GET_BY_ID = 8L;
    private static final Long CATEGORY_ID_FOR_UPDATE = 9L;
    private static final Long CATEGORY_ID_FOR_DELETE = 10L;
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
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(ADD_CATEGORIES_SQL)
            );
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
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(DELETE_CATEGORIES_SQL)
            );
        }
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(
            scripts = {ADD_CATEGORY_SQL},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {DELETE_CATEGORY_BY_NAME_SQL},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Create a new category")
    public void createCategory_ValidRequestDto_Success() throws Exception {
        CreateCategoryRequestDto dto = new CreateCategoryRequestDto();
        dto.setName("E Category");
        dto.setDescription("E Description");

        CategoryDto expected = new CategoryDto(
                5L,
                "E Category",
                "E Description");

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
    @Sql(
            scripts = ADD_CATEGORY_GET_BY_ID_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_CATEGORY_GET_BY_ID_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Retrieve a category details by id")
    public void getCategoryById_GivenDto_Success() throws Exception {
        CategoryDto expected = new CategoryDto(
                8L,
                "R Category",
                "R Description for Category R");

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get(URL_WITH_ID, CATEGORY_ID_FOR_GET_BY_ID)
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
            scripts = ADD_CATEGORY_FOR_UPDATE_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = UPDATE_CATEGORY_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_CATEGORY_FOR_UPDATE_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    @DisplayName("Update a specific category")
    public void updateCategory_GivenDto_Success() throws Exception {
        UpdateCategoryRequestDto dto = new UpdateCategoryRequestDto();
        dto.setName("T Category Upd");
        dto.setDescription("T Description for Category T");

        CategoryDto expected = new CategoryDto(
                CATEGORY_ID_FOR_UPDATE,
                dto.getName(),
                dto.getDescription()
        );

        String json = objectMapper.writeValueAsString(expected);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post(URL_WITH_ID, CATEGORY_ID_FOR_UPDATE)
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
            scripts = ADD_CATEGORY_FOR_DELETE_BY_ID_SQL,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = DELETE_CATEGORY_FOR_DELETE_BY_ID_SQL,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    public void deleteCategoryBook_GivenDto_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(URL_WITH_ID, CATEGORY_ID_FOR_DELETE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
