package shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.dto.request.shopping.cart.AddBookToCartRequestDto;
import shop.dto.request.shopping.cart.UpdateQuantityBookRequestDto;
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
    @GetMapping
    public ShoppingCartResponseDto getShoppingCartById(Authentication authentication) {
        return shoppingCartService.getShoppingCartById(authentication);
    }

    @Operation(
            summary = "Add book to shopping cart",
            description = "Add book to shopping cart"
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public ShoppingCartResponseDto addBookToShoppingCart(
            Authentication authentication, @RequestBody @Valid AddBookToCartRequestDto requestDto) {
        return shoppingCartService.addBookToShoppingCart(authentication, requestDto);
    }

    @Operation(
            summary = "Update quantity book",
            description = "Update quantity book in shopping cart"
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/items/{cartItemId}")
    public ShoppingCartResponseDto updateQuantityBook(
            @PathVariable @Positive Long cartItemId,
            @RequestBody @Valid UpdateQuantityBookRequestDto requestDto) {
        return shoppingCartService.updateQuantityBook(cartItemId, requestDto);
    }

    @Operation(
            summary = "Remove book from shopping cart",
            description = "Remove book from shopping cart"
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/items/{cartItemId}")
    public void removeBookFromShoppingCart(
            Authentication authentication, @PathVariable @Positive Long cartItemId) {
        shoppingCartService.removeBookFromShoppingCart(authentication, cartItemId);
    }
}
