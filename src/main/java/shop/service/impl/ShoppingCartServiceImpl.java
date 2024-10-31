package shop.service.impl;

import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dto.request.shopping.cart.AddBookToCartRequestDto;
import shop.dto.request.shopping.cart.UpdateQuantityBookRequestDto;
import shop.dto.responce.cart.item.CartItemResponseDto;
import shop.dto.responce.shopping.cart.ShoppingCartResponseDto;
import shop.entity.CartItem;
import shop.entity.ShoppingCart;
import shop.entity.User;
import shop.exception.EntityNotFoundException;
import shop.mapper.CartItemMapper;
import shop.mapper.ShoppingCartMapper;
import shop.repository.BookRepository;
import shop.repository.CartItemRepository;
import shop.repository.ShoppingCartRepository;
import shop.service.ShoppingCartService;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto getShoppingCartById(Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found by user id: " + userId));
        Set<CartItemResponseDto> cartItemResponseDtos = cartItemMapper
                .toCartItemResponseDtos(shoppingCart.getCartItems());
        ShoppingCartResponseDto shoppingCartResponseDto = shoppingCartMapper
                .toShoppingCartResponseDto(shoppingCart);
        shoppingCartResponseDto.setCartItems(cartItemResponseDtos);
        return shoppingCartResponseDto;
    }

    @Transactional
    @Override
    public ShoppingCartResponseDto addBookToShoppingCart(
            Authentication authentication, AddBookToCartRequestDto requestDto) {
        Long userId = getUserIdFromAuthentication(authentication);
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can`t get shopping cart for user with id : " + userId));
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        CartItem existingCartItem = cartItems.stream()
                .filter(cartItem -> cartItem.getBook().getId().equals(requestDto.getBookId()))
                .findFirst()
                .orElse(null);
        if (existingCartItem != null) {
            existingCartItem.setQuantity(requestDto.getQuantity() + existingCartItem.getQuantity());
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setShoppingCart(shoppingCart);
            cartItem.setQuantity(requestDto.getQuantity());
            cartItem.setBook(bookRepository.findById(requestDto.getBookId())
                    .orElseThrow(() -> new EntityNotFoundException("Can`t get book by id: "
                            + requestDto.getBookId())));
            cartItems.add(cartItem);
            cartItemRepository.save(cartItem);
        }
        shoppingCart.setCartItems(cartItems);
        shoppingCart = shoppingCartRepository.save(shoppingCart);
        ShoppingCartResponseDto shoppingCartResponseDto = shoppingCartMapper
                .toShoppingCartResponseDto(shoppingCart);
        shoppingCartResponseDto.setCartItems(cartItemMapper.toCartItemResponseDtos(cartItems));
        return shoppingCartResponseDto;
    }

    @Transactional
    @Override
    public ShoppingCartResponseDto updateQuantityBook(Long cartItemId,
                                                  UpdateQuantityBookRequestDto requestDto) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can`t get cart by id: " + cartItemId));
        cartItem.setQuantity(cartItem.getQuantity() + requestDto.getQuantity());
        cartItemRepository.save(cartItem);
        ShoppingCart shoppingCart = cartItem.getShoppingCart();
        ShoppingCartResponseDto shoppingCartResponseDto = shoppingCartMapper
                .toShoppingCartResponseDto(shoppingCart);
        shoppingCartResponseDto.setCartItems(
                cartItemMapper.toCartItemResponseDtos(shoppingCart.getCartItems()));
        return shoppingCartResponseDto;
    }

    @Override
    public void removeBookFromShoppingCart(Authentication authentication, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't get cart item by id: " + cartItemId));
        ShoppingCart shoppingCart = cartItem.getShoppingCart();
        if (cartItem.getQuantity() <= 1) {
            shoppingCart.removeItemFromCart(cartItem);
            cartItemRepository.delete(cartItem);
            if (shoppingCart.getCartItems().isEmpty()) {
                shoppingCart.setCartItems(new HashSet<>());
            }
        } else {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartItemRepository.save(cartItem);
        }
        shoppingCartRepository.save(shoppingCart);
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}
