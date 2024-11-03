package shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.dto.responce.order.OrderItemDto;
import shop.service.OrderItemService;

@Tag(name = "Order Item management", description = "Endpoints for managing order items")
@RestController
@RequestMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class OrderItemController {
    private final OrderItemService orderItemService;

    @Operation(
            summary = "Get all order items",
            description = "Retrieve all order items for a specific order"
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items")
    public Page<OrderItemDto> getOrderItems(Authentication authentication,
                                            @PathVariable @Positive Long orderId,
                                            Pageable pageable) {
        return orderItemService.getOrderItems(authentication, orderId, pageable);
    }

    @Operation(
            summary = "Get a order item",
            description = "Retrieve a specific order item within an order"
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto getOrderItem(Authentication authentication,
                                     @PathVariable @Positive Long orderId,
                                     @PathVariable @Positive Long itemId) {
        return orderItemService.getOrderItem(authentication, orderId, itemId);
    }
}
