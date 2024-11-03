package shop.service;

import org.springframework.security.core.Authentication;
import shop.dto.request.order.PlaceOrderRequestDto;
import shop.dto.request.order.StatusDto;
import shop.dto.responce.order.OrderResponseDto;

public interface OrderService {
    OrderResponseDto placeOrder(Authentication authentication, PlaceOrderRequestDto requestDto);

    OrderResponseDto getOrderHistory(Authentication authentication);

    OrderResponseDto updateOrderStatus(Long id, StatusDto statusDto);
}
