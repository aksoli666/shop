package shop.repository.book.spec.tools.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import shop.dto.request.BookSearchParamsDto;
import shop.entity.Book;
import shop.repository.spec.tools.SpecificationBuilder;
import shop.repository.spec.tools.SpecificationProviderManager;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String AUTHOR_KEY = "author";
    private static final String ISBN_KEY = "isbn";
    private static final String TITLE_KEY = "title";
    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParamsDto searchParams) {
        Specification<Book> defaultSpec = Specification.where(null);
        if (searchParams.author() != null && searchParams.author().length > 0) {
            defaultSpec = defaultSpec.and(specificationProviderManager
                    .getSpecificationProvider(AUTHOR_KEY)
                    .getSpecification(searchParams.author()));
        }
        if (searchParams.isbn() != null && searchParams.isbn().length > 0) {
            defaultSpec = defaultSpec.and(specificationProviderManager
                    .getSpecificationProvider(ISBN_KEY)
                    .getSpecification(searchParams.isbn()));
        }
        if (searchParams.title() != null && searchParams.title().length > 0) {
            defaultSpec = defaultSpec.and(specificationProviderManager
                    .getSpecificationProvider(TITLE_KEY)
                    .getSpecification(searchParams.title()));
        }
        return defaultSpec;
    }
}
