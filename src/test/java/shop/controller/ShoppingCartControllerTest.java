package shop.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import shop.dto.request.shopping.cart.UpdateQuantityBookRequestDto;
import shop.dto.responce.cart.item.CartItemResponseDto;
import shop.dto.responce.shopping.cart.ShoppingCartResponseDto;
import shop.entity.User;

@Sql(
        scripts = "classpath:database/shop/book/add-book-for-shopping-cart.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartControllerTest {
    private static final String ADD_CART_SQL =
            "classpath:database/shop/cart/add-cart.sql";
    private static final String ADD_ITEM_SQL =
            "classpath:database/shop/item/add-item.sql";
    private static final String ADD_USER_SQL =
            "classpath:database/shop/user/add-user.sql";
    private static final String ADD_USER_FOR_UPDATE_ITEM_SQL =
            "classpath:database/shop/user/add-user-for-update-item.sql";
    private static final String ADD_CART_FOR_UPDATE_SQL =
            "classpath:database/shop/cart/add-cart-for-update.sql";
    private static final String ADD_ITEM_FOR_UPDATE_SQL =
            "classpath:database/shop/item/add-item-for-update.sql";
    private static final String UPDATE_BOOK_QUANTITY =
            "classpath:database/shop/item/update-book-quantity.sql";
    private static final String URL_WITHOUT_ID = "/cart";
    private static final String URL_UPDATE_QUANTITY = "/cart/items/{cartItemId}";

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

    @WithMockUser(username = "user")
    @Test
    @Sql(
            scripts = {ADD_USER_SQL, ADD_CART_SQL, ADD_ITEM_SQL},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Retrieve a shopping cart by id")
    public void getShoppingCartById_GivenDto_Success() throws Exception {
        CartItemResponseDto item = new CartItemResponseDto();
        item.setId(10L);
        item.setBookId(15L);
        item.setBookTitle("N Title");
        item.setQuantity(1);

        ShoppingCartResponseDto expected = new ShoppingCartResponseDto();
        expected.setId(10L);
        expected.setUserId(10L);
        expected.setCartItems(Set.of(item));

        User user = new User();
        user.setId(10L);
        user.setPassword("pass");

        Authentication authentication = getAuthentication(user);

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get(URL_WITHOUT_ID)
                        .with(authentication(authentication))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ShoppingCartResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(),
                        ShoppingCartResponseDto.class);

        reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "user")
    @Test
    @Sql(
            scripts = {ADD_USER_FOR_UPDATE_ITEM_SQL,
                    ADD_CART_FOR_UPDATE_SQL,
                    ADD_ITEM_FOR_UPDATE_SQL},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = UPDATE_BOOK_QUANTITY,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @DisplayName("Update a book quantity")
    public void updateBookQuantity_GivenDto_Success() throws Exception {
        UpdateQuantityBookRequestDto dto = new UpdateQuantityBookRequestDto();
        dto.setQuantity(5);

        CartItemResponseDto item = new CartItemResponseDto();
        item.setId(11L);
        item.setBookId(15L);
        item.setBookTitle("N Title");
        item.setQuantity(5);

        ShoppingCartResponseDto expected = new ShoppingCartResponseDto();
        expected.setId(11L);
        expected.setUserId(11L);
        expected.setCartItems(Set.of(item));

        User user = new User();
        user.setId(11L);
        user.setPassword("pass11");

        Authentication authentication = getAuthentication(user);

        String json = objectMapper.writeValueAsString(dto);

        MvcResult result = mockMvc.perform(
                        MockMvcRequestBuilders.put(URL_UPDATE_QUANTITY, 11L)
                                .with(authentication(authentication))
                                .content(json)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        ShoppingCartResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), ShoppingCartResponseDto.class);

        reflectionEquals(expected, actual);
    }

    private Authentication getAuthentication(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(
                user, user.getPassword(), authorities);
    }
}
