package shop.repository.role;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
