package shop.repository.role;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(Role.RoleName role);
}
