package shop.service;

import org.springframework.security.core.Authentication;
import shop.dto.request.shopping.cart.AddBookToCartRequestDto;
import shop.dto.request.shopping.cart.UpdateQuantityBookRequestDto;
import shop.dto.responce.shopping.cart.ShoppingCartResponseDto;
import shop.entity.User;

public interface ShoppingCartService {
    void createShoppingCart(User user);

    ShoppingCartResponseDto getShoppingCartById(Authentication authentication);

    ShoppingCartResponseDto addBookToShoppingCart(Authentication authentication,
                                                  AddBookToCartRequestDto requestDto);

    ShoppingCartResponseDto updateQuantityBook(Long cartItemId,
                                           UpdateQuantityBookRequestDto requestDto);

    void removeBookFromShoppingCart(Authentication authentication, Long cartItemId);
}
