package shop.service.impl;

import static shop.entity.User.getUserIdFromAuthentication;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import shop.dto.request.order.PlaceOrderRequestDto;
import shop.dto.request.order.StatusDto;
import shop.dto.responce.cart.item.CartItemResponseDto;
import shop.dto.responce.order.OrderResponseDto;
import shop.dto.responce.shopping.cart.ShoppingCartResponseDto;
import shop.entity.Book;
import shop.entity.Order;
import shop.entity.OrderItem;
import shop.entity.Status;
import shop.entity.User;
import shop.exception.EntityNotFoundException;
import shop.mapper.OrderMapper;
import shop.repository.BookRepository;
import shop.repository.OrderItemRepository;
import shop.repository.OrderRepository;
import shop.repository.StatusRepository;
import shop.security.CustomUserDetailsService;
import shop.service.OrderService;
import shop.service.ShoppingCartService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartService shoppingCartService;
    private final BookRepository bookRepository;
    private final CustomUserDetailsService userDetailsService;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final StatusRepository statusRepository;
    private final OrderMapper orderMapper;

    @Transactional
    @Override
    public OrderResponseDto placeOrder(Authentication authentication,
                                       PlaceOrderRequestDto requestDto) {
        User user = (User)userDetailsService.loadUserByUsername(authentication.getName());
        Order order = new Order();
        order.setUser(user);
        order.setStatus(statusRepository.findByStatus(Status.StatusName.COMPLETED)
                .orElseThrow(() -> new EntityNotFoundException("Can`t find status: "
                        + Status.StatusName.COMPLETED)));
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.getShippingAddress());
        Set<OrderItem> orderItems = new HashSet<>();
        BigDecimal total = BigDecimal.ZERO;
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
            total = total.add(orderItem.getPrice());
        }
        order.setOrderItems(orderItems);
        order.setTotal(total);
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

    @Transactional
    @Override
    public OrderResponseDto updateOrderStatus(Long id, StatusDto statusDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can`t find order by id: "
                        + id));
        order.setStatus(statusRepository.findByStatus(
                        Status.StatusName.valueOf(statusDto.getStatus()))
                        .orElseThrow(() -> new EntityNotFoundException("Can`t find status:"
                                + statusDto)));
        orderRepository.save(order);
        return orderMapper.toOrderResponseDto(order);
    }
}
