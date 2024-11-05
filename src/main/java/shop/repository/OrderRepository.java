package shop.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = {"orderItems", "status"})
    Optional<Order> findOrderById(Long id);

    @EntityGraph(attributePaths = {"orderItems", "status"})
    Optional<Order> findOrderByUserId(Long userId);
}
