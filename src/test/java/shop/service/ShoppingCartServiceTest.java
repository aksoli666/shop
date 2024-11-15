package shop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import shop.dto.request.shopping.cart.AddBookToCartRequestDto;
import shop.dto.request.shopping.cart.UpdateQuantityBookRequestDto;
import shop.dto.responce.cart.item.CartItemResponseDto;
import shop.dto.responce.shopping.cart.ShoppingCartResponseDto;
import shop.entity.Book;
import shop.entity.CartItem;
import shop.entity.Category;
import shop.entity.Role;
import shop.entity.ShoppingCart;
import shop.entity.User;
import shop.exception.EntityNotFoundException;
import shop.mapper.ShoppingCartMapper;
import shop.repository.BookRepository;
import shop.repository.CartItemRepository;
import shop.repository.ShoppingCartRepository;
import shop.service.impl.ShoppingCartServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    private static final Long ID_1L_CORRECT = 1L;
    private static final Long ID_2L_CORRECT = 2L;
    private static final Long INCORRECT_ID = -100L;
    private static final int QUANTITY = 1;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private Authentication authentication;

    @Test
    @DisplayName("Verify createShoppingCart(...) creates and saves a new ShoppingCart")
    public void createShoppingCart_ValidUser_SavesShoppingCart() {
        User expected = new User();
        shoppingCartService.createShoppingCart(expected);
        ArgumentCaptor<ShoppingCart> captor = ArgumentCaptor.forClass(ShoppingCart.class);
        verify(shoppingCartRepository).save(captor.capture());
        User actual = captor.getValue().getUser();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify getShoppingCartById(...), return ShoppingCartResponseDto")
    public void createShoppingCart_ValidUser_returnNothing() {
        User user = createUser();
        ShoppingCart shoppingCart = createShoppingCart();
        ShoppingCartResponseDto expected = createShoppingCartResponseDto();

        when(authentication.getPrincipal()).thenReturn(user);
        when(shoppingCartRepository.findByUserId(ID_1L_CORRECT))
                .thenReturn(Optional.of(shoppingCart));
        when(shoppingCartMapper.toShoppingCartResponseDto(shoppingCart))
                .thenReturn(expected);

        ShoppingCartResponseDto actual = shoppingCartService
                .getShoppingCartById(authentication);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify getShoppingCartById(...), throw EntityNotFoundException")
    public void getShoppingCartById_InvalidId_throwEntityNotFoundException() {
        User user = createUser();
        user.setId(INCORRECT_ID);

        when(authentication.getPrincipal()).thenReturn(user);
        when(shoppingCartRepository.findByUserId(INCORRECT_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.getShoppingCartById(authentication));
    }

    @DisplayName("Verify addBookToShoppingCart(...), return ShoppingCartResponseDto")
    @Test
    public void addBookToShoppingCart_validArgs_returnResponseDto() {
        AddBookToCartRequestDto requestDto = new AddBookToCartRequestDto();
        requestDto.setBookId(ID_2L_CORRECT);
        requestDto.setQuantity(QUANTITY);

        User user = createUser();
        ShoppingCart shoppingCart = createShoppingCart();
        Book book = createBook();
        CartItem newCartItem = createCartItem();

        ShoppingCartResponseDto expected = createShoppingCartResponseDto();
        expected.setCartItems(Set.of(toCartItemResponseDto(newCartItem)));

        when(authentication.getPrincipal()).thenReturn(user);
        when(shoppingCartRepository.findShoppingCartById(ID_1L_CORRECT))
                .thenReturn(Optional.of(shoppingCart));
        when(bookRepository.findBookById(ID_2L_CORRECT))
                .thenReturn(Optional.of(book));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(newCartItem);
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(shoppingCart);
        when(shoppingCartMapper.toShoppingCartResponseDto(any(ShoppingCart.class)))
                .thenReturn(expected);

        ShoppingCartResponseDto actual = shoppingCartService
                .addBookToShoppingCart(authentication, requestDto);

        assertEquals(expected, actual);
    }

    @DisplayName("Verify addBookToShoppingCart(...), throw EntityNotFoundException")
    @Test
    public void addBookToShoppingCart_InvalidShoppingCartId_throwEntityNotFoundException() {
        AddBookToCartRequestDto requestDto = new AddBookToCartRequestDto();
        requestDto.setBookId(ID_1L_CORRECT);
        requestDto.setQuantity(QUANTITY);

        User user = createUser();
        user.setId(INCORRECT_ID);

        when(authentication.getPrincipal()).thenReturn(user);
        when(shoppingCartRepository.findShoppingCartById(INCORRECT_ID))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService
                        .addBookToShoppingCart(authentication, requestDto));
    }

    @DisplayName("Verify addBookToShoppingCart(...), book already exists in cart")
    @Test
    public void addBookToShoppingCart_BookAlreadyExists_throwIllegalArgumentException() {
        AddBookToCartRequestDto requestDto = new AddBookToCartRequestDto();
        requestDto.setBookId(ID_1L_CORRECT);
        requestDto.setQuantity(QUANTITY);

        User user = createUser();
        ShoppingCart shoppingCart = createShoppingCart();
        CartItem existingCartItem = new CartItem();
        existingCartItem.setBook(new Book());
        existingCartItem.getBook().setId(ID_1L_CORRECT);
        shoppingCart.getCartItems().add(existingCartItem);

        when(authentication.getPrincipal()).thenReturn(user);
        when(shoppingCartRepository.findShoppingCartById(ID_1L_CORRECT))
                .thenReturn(Optional.of(shoppingCart));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> shoppingCartService
                        .addBookToShoppingCart(authentication, requestDto));

        assertEquals("Book already exists. Id: " + ID_1L_CORRECT, exception.getMessage());
    }

    @DisplayName("Verify addBookToShoppingCart(...), book not exists in cart")
    @Test
    public void addBookToShoppingCart_bookNotFound_throwEntityNotFoundException() {
        AddBookToCartRequestDto requestDto = new AddBookToCartRequestDto();
        requestDto.setBookId(INCORRECT_ID);
        requestDto.setQuantity(1);

        User user = createUser();
        ShoppingCart shoppingCart = createShoppingCart();

        when(authentication.getPrincipal()).thenReturn(user);
        when(shoppingCartRepository.findShoppingCartById(ID_1L_CORRECT))
                .thenReturn(Optional.of(shoppingCart));
        when(bookRepository.findBookById(INCORRECT_ID))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.addBookToShoppingCart(authentication, requestDto));

        assertEquals("Can`t find book by id: " + INCORRECT_ID, exception.getMessage());
    }

    @Test
    @DisplayName("Verify updateBookQuantity(...), return ShoppingCartResponseDto")
    public void updateQuantityBook_ValidArgs_returnShoppingCartResponseDto() {
        UpdateQuantityBookRequestDto requestDto = new UpdateQuantityBookRequestDto();
        requestDto.setQuantity(5);

        CartItem cartItem = createCartItem();
        ShoppingCart shoppingCart = createShoppingCart();
        cartItem.setShoppingCart(shoppingCart);

        CartItem updatedCartItem = createCartItem();
        updatedCartItem.setQuantity(cartItem.getQuantity() + requestDto.getQuantity());

        ShoppingCartResponseDto expected = createShoppingCartResponseDto();
        expected.setCartItems(Set.of(toCartItemResponseDto(updatedCartItem)));

        when(cartItemRepository.findById(ID_1L_CORRECT)).thenReturn(Optional.of(cartItem));
        when(cartItemRepository.save(cartItem)).thenReturn(updatedCartItem);
        when(shoppingCartMapper.toShoppingCartResponseDto(shoppingCart)).thenReturn(expected);

        ShoppingCartResponseDto actual = shoppingCartService.updateQuantityBook(ID_1L_CORRECT, requestDto);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify updateBookQuantity(...), throw EntityNotFoundException")
    public void updateQuantityBook_invalidCartItemId_throwEntityNotFoundException() {
        when(cartItemRepository.findById(INCORRECT_ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.updateQuantityBook(INCORRECT_ID,
                        new UpdateQuantityBookRequestDto()));
    }

    @Test
    @DisplayName("Verify removeBookFromShoppingCart(...), void")
    public void removeBookFromShoppingCart_ValidArgs_void() {
        ShoppingCart expected = createShoppingCart();
        expected.setCartItems(Collections.emptySet());
        CartItem cartItem = createCartItem();
        cartItem.setShoppingCart(expected);

        when(cartItemRepository.findById(ID_1L_CORRECT)).thenReturn(Optional.of(cartItem));

        shoppingCartService.removeBookFromShoppingCart(authentication, ID_1L_CORRECT);

        verify(cartItemRepository).delete(cartItem);
        ArgumentCaptor<ShoppingCart> shoppingCartCaptor = ArgumentCaptor.forClass(ShoppingCart.class);
        verify(shoppingCartRepository).save(shoppingCartCaptor.capture());

        ShoppingCart actual = shoppingCartCaptor.getValue();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify removeBookFromShoppingCart(...), throw EntityNotFoundException")
    public void removeBookFromShoppingCart_invalidCartItemId_throwEntityNotFoundException() {
        when(cartItemRepository.findById(INCORRECT_ID)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,
                () -> shoppingCartService.removeBookFromShoppingCart(authentication,
                        INCORRECT_ID));
    }

    private User createUser() {
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

    private Book createBook() {
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

    private CartItem createCartItem() {
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

    private ShoppingCart createShoppingCart() {
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

    private CartItemResponseDto createCartItemResponseDto() {
        CartItemResponseDto itemDto = new CartItemResponseDto();
        itemDto.setId(ID_1L_CORRECT);
        itemDto.setBookId(ID_1L_CORRECT);
        itemDto.setBookTitle("Title");
        itemDto.setQuantity(QUANTITY);

        return itemDto;
    }

    private ShoppingCartResponseDto createShoppingCartResponseDto() {
        ShoppingCartResponseDto cartDto = new ShoppingCartResponseDto();
        cartDto.setId(ID_1L_CORRECT);
        cartDto.setUserId(ID_1L_CORRECT);
        cartDto.setCartItems(Set.of(createCartItemResponseDto()));
        return cartDto;
    }

    private CartItemResponseDto toCartItemResponseDto(CartItem cartItem) {
        CartItemResponseDto dto = new CartItemResponseDto();
        dto.setId(cartItem.getId());
        dto.setBookId(cartItem.getBook().getId());
        dto.setQuantity(cartItem.getQuantity());
        return dto;
    }
}
