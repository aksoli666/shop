package shop.repository.spec.tools.book.spec.tools.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import shop.entity.Book;
import shop.repository.spec.tools.SpecificationProvider;
import shop.repository.spec.tools.SpecificationProviderManager;

@Component
@RequiredArgsConstructor
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> specificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return specificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "No specification provider found for key: " + key)
                );
    }
}
