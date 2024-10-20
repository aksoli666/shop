package shop.repository.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
