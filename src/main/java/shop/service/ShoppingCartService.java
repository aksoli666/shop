package shop.service;

import shop.dto.request.shopping.cart.AddBookToCartRequestDto;
import shop.dto.request.shopping.cart.UpdateQuantityBookRequestDto;
import shop.dto.responce.cart.item.CartItemResponseDto;
import shop.dto.responce.shopping.cart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCartById(Long id);

    ShoppingCartResponseDto addBookToShoppingCart(Long id, AddBookToCartRequestDto requestDto);

    CartItemResponseDto updateQuantityBook(Long cartItemId,
                                           UpdateQuantityBookRequestDto requestDto);

    void removeBookFromShoppingCart(Long cartItemId, Long bookId);
}
