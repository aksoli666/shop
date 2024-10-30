package shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.dto.request.shopping.cart.AddBookToCartRequestDto;
import shop.dto.request.shopping.cart.UpdateQuantityBookRequestDto;
import shop.dto.responce.cart.item.CartItemResponseDto;
import shop.dto.responce.shopping.cart.ShoppingCartResponseDto;
import shop.service.ShoppingCartService;

@RestController
@RequestMapping(value = "/cart", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(
            summary = "Get a shopping cart by id",
            description = "Retrieve a cart details by id"
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public ShoppingCartResponseDto getShoppingCartById(@PathVariable Long id) {
        return shoppingCartService.getShoppingCartById(id);
    }

    @Operation(
            summary = "Add book to shopping cart",
            description = "Add book to shopping cart"
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/{id}")
    public ShoppingCartResponseDto addBookToShoppingCart(
            @PathVariable Long id, @RequestBody AddBookToCartRequestDto requestDto) {
        return shoppingCartService.addBookToShoppingCart(id, requestDto);
    }

    @Operation(
            summary = "Update quantity book",
            description = "Update quantity book in shopping cart"
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/items/{cartItemId}")
    public CartItemResponseDto updateQuantityBook(
            @PathVariable Long cartItemId,
            @RequestBody UpdateQuantityBookRequestDto requestDto) {
        return shoppingCartService.updateQuantityBook(cartItemId, requestDto);
    }

    @Operation(
            summary = "Remove book from shopping cart",
            description = "Remove book from shopping cart"
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/items/{cartItemId}/{bookId}")
    public void removeBookFromShoppingCart(
            @PathVariable Long cartItemId, @PathVariable Long bookId) {
        shoppingCartService.removeBookFromShoppingCart(cartItemId, bookId);
    }
}
