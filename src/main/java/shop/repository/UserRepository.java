package shop.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByEmail(String email);

    @EntityGraph(attributePaths = {"roles"})
    Optional<User> findUserByEmail(String email);
}
