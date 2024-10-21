package shop.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmail(String email);
}
