package shop.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import shop.dto.responce.order.OrderItemDto;

public interface OrderItemService {
    Page<OrderItemDto> getOrderItems(Authentication authentication,
                                     Long orderId, Pageable pageable);

    OrderItemDto getOrderItem(Authentication authentication, Long orderId, Long itemId);
}
