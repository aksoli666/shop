package shop.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Page<OrderItem> findByOrderIdAndOrderUserId(Long orderId, Long userId, Pageable pageable);

    List<OrderItem> findByOrderIdAndOrderUserId(Long orderId, Long userId);
}
