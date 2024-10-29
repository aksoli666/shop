package shop.repository.spec.tools.book.prov.filters;

import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import shop.entity.Book;
import shop.repository.spec.tools.SpecificationProvider;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    private static final String TITLE_KEY = "title";

    @Override
    public String getKey() {
        return TITLE_KEY;
    }

    public Specification<Book> getSpecification(String[] titles) {
        return (root, query, criteriaBuilder) -> root.get(TITLE_KEY).in(Arrays.asList(titles));
    }
}
