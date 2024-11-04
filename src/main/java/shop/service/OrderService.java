package shop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import shop.dto.request.order.PlaceOrderRequestDto;
import shop.dto.request.order.StatusDto;
import shop.dto.responce.order.OrderItemDto;
import shop.dto.responce.order.OrderResponseDto;

public interface OrderService {
    OrderResponseDto placeOrder(Authentication authentication, PlaceOrderRequestDto requestDto);

    OrderResponseDto getOrderHistory(Authentication authentication);

    OrderResponseDto updateOrderStatus(Long id, StatusDto statusDto);

    Page<OrderItemDto> getOrderItems(Authentication authentication,
                                     Long orderId, Pageable pageable);

    OrderItemDto getOrderItem(Authentication authentication, Long orderId, Long itemId);
}
