package shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
