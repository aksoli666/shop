package shop.service.impl;

import static shop.security.CustomUserDetailsService.getUserIdFromAuthentication;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dto.request.shopping.cart.AddBookToCartRequestDto;
import shop.dto.request.shopping.cart.UpdateQuantityBookRequestDto;
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
        return shoppingCartMapper.toShoppingCartResponseDto(shoppingCart);
    }

    @Transactional
    @Override
    public ShoppingCartResponseDto addBookToShoppingCart(
            Authentication authentication, AddBookToCartRequestDto requestDto) {
        Long userId = getUserIdFromAuthentication(authentication);
        Long bookId = requestDto.getBookId();
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can`t get shopping cart for user with id : " + userId));
        boolean bookExists = shoppingCart.getCartItems().stream()
                .anyMatch(cartItem -> cartItem.getBook().getId().equals(bookId));
        if (bookExists) {
            throw new IllegalArgumentException("Book already exists. Id: " + bookId);
        }
        CartItem cartItem = new CartItem();
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setQuantity(requestDto.getQuantity());
        cartItem.setBook(bookRepository.findBookById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Can`t find book by id: "
                        + bookId)));
        shoppingCart.getCartItems().add(cartItem);
        cartItemRepository.save(cartItem);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toShoppingCartResponseDto(shoppingCart);
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
        return shoppingCartMapper
                .toShoppingCartResponseDto(cartItem.getShoppingCart());
    }

    @Transactional
    @Override
    public void removeBookFromShoppingCart(Authentication authentication, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't get cart item by id: " + cartItemId));
        ShoppingCart shoppingCart = cartItem.getShoppingCart();
        shoppingCart.removeItemFromCart(cartItem);
        cartItemRepository.delete(cartItem);
        shoppingCartRepository.save(shoppingCart);
    }
}
