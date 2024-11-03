package shop.service.impl;

import static shop.entity.User.getUserIdFromAuthentication;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import shop.dto.responce.order.OrderItemDto;
import shop.entity.OrderItem;
import shop.exception.EntityNotFoundException;
import shop.mapper.OrderItemMapper;
import shop.repository.OrderItemRepository;
import shop.service.OrderItemService;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public Page<OrderItemDto> getOrderItems(Authentication authentication,
                                            Long orderId, Pageable pageable) {
        Long userId = getUserIdFromAuthentication(authentication);
        Page<OrderItem> orderItems = orderItemRepository
                .findByOrderIdAndOrderUserId(orderId, userId, pageable);
        return orderItems.map(orderItemMapper::toDto);
    }

    @Override
    public OrderItemDto getOrderItem(Authentication authentication, Long orderId, Long itemId) {
        Long userId = getUserIdFromAuthentication(authentication);
        List<OrderItem> orderItems = orderItemRepository
                .findByOrderIdAndOrderUserId(userId, orderId);
        OrderItem orderItem = orderItems.stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Can't find OrderItem by id "
                        + itemId));
        return orderItemMapper.toDto(orderItem);
    }
}
