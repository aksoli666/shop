package shop.repository;

import java.math.BigDecimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Book b SET "
            + "b.title = :title, "
            + "b.author = :author, "
            + "b.isbn = :isbn, "
            + "b.price = :price, "
            + "b.description = :description, "
            + "b.coverImage = :coverImage "
            + "WHERE b.id = :id")
    int updateBookById(Long id,
                       String title,
                       String author,
                       String isbn,
                       BigDecimal price,
                       String description,
                       String coverImage);
}
