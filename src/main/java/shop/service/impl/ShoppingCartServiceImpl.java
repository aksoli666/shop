package shop.service.impl;

import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.dto.request.shopping.cart.AddBookToCartRequestDto;
import shop.dto.request.shopping.cart.UpdateQuantityBookRequestDto;
import shop.dto.responce.cart.item.CartItemResponseDto;
import shop.dto.responce.shopping.cart.ShoppingCartResponseDto;
import shop.entity.CartItem;
import shop.entity.ShoppingCart;
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

    @Transactional
    @Override
    public ShoppingCartResponseDto getShoppingCartById(Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can`t get shopping cart by id: " + id));
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
            Long id, AddBookToCartRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can`t get shopping cart by id: " + id));
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
            cartItem = cartItemRepository.save(cartItem);
            cartItems.add(cartItem);
        }
        shoppingCart.setCartItems(cartItems);
        shoppingCart = shoppingCartRepository.save(shoppingCart);
        ShoppingCartResponseDto shoppingCartResponseDto = shoppingCartMapper
                .toShoppingCartResponseDto(shoppingCart);
        shoppingCartResponseDto.setCartItems(cartItemMapper.toCartItemResponseDtos(cartItems));
        return shoppingCartResponseDto;
    }

    @Override
    public CartItemResponseDto updateQuantityBook(Long cartItemId,
                                                  UpdateQuantityBookRequestDto requestDto) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can`t get cart by id: " + cartItemId));
        CartItem updated = cartItem;
        updated.setQuantity(cartItem.getQuantity() + requestDto.getQuantity());
        cartItemMapper.updateCartItem(cartItem, updated);
        return cartItemMapper.toCartItemResponseDto(cartItemRepository.save(updated));
    }

    @Transactional
    @Override
    public void removeBookFromShoppingCart(Long cartItemId, Long bookId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't get cart item by id: " + cartItemId));
        if (!cartItem.getBook().getId().equals(bookId)) {
            throw new EntityNotFoundException("Can't delete book (id: "
                    + bookId + ") from cart item (id: " + cartItemId + ")");
        }
        ShoppingCart shoppingCart = cartItem.getShoppingCart();
        if (cartItem.getQuantity() <= 1) {
            shoppingCart.getCartItems().remove(cartItem);
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
}
