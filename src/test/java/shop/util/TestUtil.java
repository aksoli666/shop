package shop.util;

import shop.dto.request.book.UpdateBookRequestDto;
import shop.dto.responce.book.BookDto;
import shop.dto.responce.cart.item.CartItemResponseDto;
import shop.dto.responce.category.CategoryDto;
import shop.dto.responce.shopping.cart.ShoppingCartResponseDto;
import shop.entity.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TestUtil {
    private static final Long ID_1L_CORRECT = 1L;
    private static final String CATEGORY_NAME_CORRECT = "Test Category";
    private static final String CATEGORY_DESCRIPTION_CORRECT = "Test Description";
    private static final String BOOK_TITLE_CORRECT = "Test Book";
    private static final String BOOK_AUTHOR_CORRECT = "Test Author";
    private static final String BOOK_ISBN_CORRECT = "Test Isbn";
    private static final BigDecimal BOOK_PRICE_CORRECT = BigDecimal.TEN;
    private static final int QUANTITY = 1;

    public static Category createCategory() {
        Category category = new Category();
        category.setName(CATEGORY_NAME_CORRECT);
        return category;
    }

    public static CategoryDto createCategoryDto() {
        return new CategoryDto(
                ID_1L_CORRECT,
                CATEGORY_NAME_CORRECT,
                CATEGORY_DESCRIPTION_CORRECT);
    }

    public static Book createBookId() {
        Category category = createCategory();

        Book book = new Book();
        book.setTitle(BOOK_TITLE_CORRECT);
        book.setAuthor(BOOK_AUTHOR_CORRECT);
        book.setIsbn(BOOK_ISBN_CORRECT);
        book.setPrice(BOOK_PRICE_CORRECT);
        book.setCategories(new HashSet<>(Set.of(category)));
        return book;
    }

    public static BookDto createBookDto() {
        BookDto dto = new BookDto();
        dto.setId(ID_1L_CORRECT);
        dto.setTitle(BOOK_TITLE_CORRECT);
        dto.setAuthor(BOOK_AUTHOR_CORRECT);
        dto.setIsbn(BOOK_ISBN_CORRECT);
        dto.setPrice(BOOK_PRICE_CORRECT);
        dto.setCategoryIds(new HashSet<>(Set.of(ID_1L_CORRECT)));
        return dto;
    }

    public static UpdateBookRequestDto createUpdateBookRequestDto() {
        UpdateBookRequestDto dto = new UpdateBookRequestDto();
        dto.setTitle(BOOK_TITLE_CORRECT);
        dto.setAuthor(BOOK_AUTHOR_CORRECT);
        dto.setIsbn(BOOK_ISBN_CORRECT);
        dto.setPrice(BOOK_PRICE_CORRECT);
        dto.setCategoryIds(new HashSet<>(Set.of(ID_1L_CORRECT)));
        return dto;
    }

    public static User createUser() {
        Role role = new Role();
        role.setId(ID_1L_CORRECT);
        role.setRole(Role.RoleName.ROLE_USER);

        User user = new User();
        user.setId(ID_1L_CORRECT);
        user.setFirstName("Name");
        user.setLastName("LastName");
        user.setEmail("email@gmail.com");
        user.setPassword("password");
        user.setShippingAddress("address");
        user.setRoles(Set.of(role));

        return user;
    }

    public static Book createBook() {
        Category category = new Category();
        category.setId(ID_1L_CORRECT);
        category.setName("Name");

        Book book = new Book();
        book.setId(ID_1L_CORRECT);
        book.setTitle("Title");
        book.setAuthor("Author");
        book.setIsbn("isbn");
        book.setPrice(BigDecimal.TEN);
        book.setCategories(Set.of(category));

        return book;
    }

    public static CartItem createCartItem() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(ID_1L_CORRECT);
        shoppingCart.setUser(createUser());

        CartItem cartItem = new CartItem();
        cartItem.setId(ID_1L_CORRECT);
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(createBook());
        cartItem.setQuantity(QUANTITY);

        shoppingCart.setCartItems(Collections.singleton(cartItem));

        return cartItem;
    }

    public static ShoppingCart createShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(ID_1L_CORRECT);
        shoppingCart.setUser(createUser());

        Set<CartItem> cartItemSet = new HashSet<>();

        CartItem cartItem = new CartItem();
        cartItem.setId(ID_1L_CORRECT);
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(createBook());
        cartItem.setQuantity(QUANTITY);

        cartItemSet.add(cartItem);
        shoppingCart.setCartItems(cartItemSet);

        return shoppingCart;
    }

    public static CartItemResponseDto createCartItemResponseDto() {
        CartItemResponseDto itemDto = new CartItemResponseDto();
        itemDto.setId(ID_1L_CORRECT);
        itemDto.setBookId(ID_1L_CORRECT);
        itemDto.setBookTitle("Title");
        itemDto.setQuantity(QUANTITY);

        return itemDto;
    }

    public static ShoppingCartResponseDto createShoppingCartResponseDto() {
        ShoppingCartResponseDto cartDto = new ShoppingCartResponseDto();
        cartDto.setId(ID_1L_CORRECT);
        cartDto.setUserId(ID_1L_CORRECT);
        cartDto.setCartItems(Set.of(createCartItemResponseDto()));
        return cartDto;
    }

    public static CartItemResponseDto toCartItemResponseDto(CartItem cartItem) {
        CartItemResponseDto dto = new CartItemResponseDto();
        dto.setId(cartItem.getId());
        dto.setBookId(cartItem.getBook().getId());
        dto.setQuantity(cartItem.getQuantity());
        return dto;
    }
}
