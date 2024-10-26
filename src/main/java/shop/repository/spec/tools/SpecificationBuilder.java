package shop.repository.spec.tools;

import org.springframework.data.jpa.domain.Specification;
import shop.dto.request.book.BookSearchParamsDto;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookSearchParamsDto searchParams);
}
