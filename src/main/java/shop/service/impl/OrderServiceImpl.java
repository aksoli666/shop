package shop.service.impl;

import static shop.security.CustomUserDetailsService.getUserIdFromAuthentication;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import shop.dto.request.order.PlaceOrderRequestDto;
import shop.dto.request.order.StatusDto;
import shop.dto.responce.cart.item.CartItemResponseDto;
import shop.dto.responce.order.OrderItemDto;
import shop.dto.responce.order.OrderResponseDto;
import shop.dto.responce.shopping.cart.ShoppingCartResponseDto;
import shop.entity.Book;
import shop.entity.Order;
import shop.entity.OrderItem;
import shop.entity.User;
import shop.exception.EntityNotFoundException;
import shop.mapper.OrderItemMapper;
import shop.mapper.OrderMapper;
import shop.repository.BookRepository;
import shop.repository.OrderItemRepository;
import shop.repository.OrderRepository;
import shop.security.CustomUserDetailsService;
import shop.service.OrderService;
import shop.service.ShoppingCartService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final CustomUserDetailsService userDetailsService;
    private final ShoppingCartService shoppingCartService;
    private final BookRepository bookRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Transactional
    @Override
    public OrderResponseDto placeOrder(Authentication authentication,
                                       PlaceOrderRequestDto requestDto) {
        User user = (User) userDetailsService.loadUserByUsername(authentication.getName());
        Order order = createOrder(user, requestDto);
        Set<OrderItem> orderItems = createOrderItems(authentication, order);
        order.setOrderItems(orderItems);
        order.setTotal(getTotal(orderItems));
        orderRepository.save(order);
        orderItemRepository.saveAll(orderItems);
        return orderMapper.toOrderResponseDto(order);
    }

    @Override
    public OrderResponseDto getOrderHistory(Authentication authentication) {
        Long userId = getUserIdFromAuthentication(authentication);
        return orderMapper.toOrderResponseDto(
                orderRepository.findOrderByUserId(userId)
                        .orElseThrow(() -> new EntityNotFoundException("Can`t find order: "
                                + userId)));
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long id, StatusDto statusDto) {
        Order order = orderRepository.findOrderById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can`t find order by id: "
                        + id));
        order.setStatus(Order.Status.valueOf(statusDto.getStatus()));
        orderRepository.save(order);
        return orderMapper.toOrderResponseDto(order);
    }

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

    private Order createOrder(User user, PlaceOrderRequestDto requestDto) {
        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.getShippingAddress());
        return order;
    }

    private Set<OrderItem> createOrderItems(Authentication authentication, Order order) {
        Set<OrderItem> orderItems = new HashSet<>();
        ShoppingCartResponseDto shoppingCart = shoppingCartService
                .getShoppingCartById(authentication);
        Set<CartItemResponseDto> cartItems = shoppingCart.getCartItems();
        for (CartItemResponseDto cartItem : cartItems) {
            Book book = bookRepository.findBookById(cartItem.getBookId())
                    .orElseThrow(() -> new EntityNotFoundException("Can`t find book: "
                            + cartItem.getBookId()));
            BigDecimal price = book.getPrice();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(price.multiply(BigDecimal.valueOf(
                    cartItem.getQuantity())));
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    private BigDecimal getTotal(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
