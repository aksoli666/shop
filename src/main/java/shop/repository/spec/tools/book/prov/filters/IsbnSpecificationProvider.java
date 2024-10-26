package shop.repository.spec.tools.book.prov.filters;

import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import shop.entity.Book;
import shop.repository.spec.tools.SpecificationProvider;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book> {
    private static final String ISBN_KEY = "isbn";

    @Override
    public String getKey() {
        return ISBN_KEY;
    }

    public Specification<Book> getSpecification(String[] isbns) {
        return (root, query, criteriaBuilder) -> root.get(ISBN_KEY).in(Arrays.stream(isbns));
    }
}
