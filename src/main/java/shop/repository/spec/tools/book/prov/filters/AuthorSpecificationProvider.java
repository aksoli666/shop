package shop.repository.spec.tools.book.prov.filters;

import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import shop.entity.Book;
import shop.repository.spec.tools.SpecificationProvider;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    private static final String AUTHOR_KEY = "author";

    @Override
    public String getKey() {
        return AUTHOR_KEY;
    }

    public Specification<Book> getSpecification(String[] authors) {
        return (root, query, criteriaBuilder) -> root.get(AUTHOR_KEY).in(Arrays.stream(authors));
    }
}
