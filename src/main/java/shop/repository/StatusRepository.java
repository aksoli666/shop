package shop.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import shop.entity.Status;

public interface StatusRepository extends JpaRepository<Status, Long> {
    Optional<Status> findByStatus(Status.StatusName status);
}
