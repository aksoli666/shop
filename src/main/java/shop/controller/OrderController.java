package shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.dto.request.order.PlaceOrderRequestDto;
import shop.dto.request.order.StatusDto;
import shop.dto.responce.order.OrderResponseDto;
import shop.service.OrderService;

@Tag(name = "Order management", description = "Endpoints for managing order")
@RestController
@RequestMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(
            summary = "Create a new order",
            description = "Place an order"
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public OrderResponseDto placeOrder(Authentication authentication,
                                       @RequestBody @Valid PlaceOrderRequestDto requestDto) {
        return orderService.placeOrder(authentication, requestDto);
    }

    @Operation(
            summary = "Get all order",
            description = "Retrieve user's order history"
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public OrderResponseDto getOrderHistory(Authentication authentication) {
        return orderService.getOrderHistory(authentication);
    }

    @Operation(
            summary = "Update order status",
            description = "Update order status"
    )
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public OrderResponseDto updateOrderStatus(@PathVariable @Positive Long id,
                                              @RequestBody @Valid StatusDto statusDto) {
        return orderService.updateOrderStatus(id, statusDto);
    }
}
