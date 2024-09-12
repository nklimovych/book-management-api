package com.book.management.repository.book;

import com.book.management.model.Book;
import com.book.management.repository.SpecificationProvider;
import com.book.management.repository.SpecificationProviderManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                                         .filter(p -> p.getKey().equals(key))
                                         .findFirst()
                                         .orElseThrow(() -> new RuntimeException(
                                                 "Can't find specification provider for " + key));
    }
}
