package com.book.management.repository.book;

import com.book.management.dto.BookSearchParametersDto;
import com.book.management.model.Book;
import com.book.management.repository.SpecificationBuilder;
import com.book.management.repository.SpecificationProvider;
import com.book.management.repository.SpecificationProviderManager;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private static final String TITLE = BookSearchParameter.TITLE.getKey();
    private static final String AUTHOR = BookSearchParameter.AUTHOR.getKey();
    private static final String GENRE = BookSearchParameter.GENRE.getKey();

    private final SpecificationProviderManager<Book> specificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto paramsDto) {
        Map<String, Function<BookSearchParametersDto, Specification<Book>>>
                specificationMap = Map.of(
                TITLE, params -> params.title() != null ? getSpecification(params, TITLE) : null,
                AUTHOR, params -> params.author() != null ? getSpecification(params, AUTHOR) : null,
                GENRE, params -> params.genre() != null ? getSpecification(params, GENRE) : null
        );

        return specificationMap.values().stream()
                               .map(func -> func.apply(paramsDto))
                               .filter(Objects::nonNull)
                               .reduce(Specification.where(null), Specification::and);
    }

    private Specification<Book> getSpecification(BookSearchParametersDto params, String key) {
        SpecificationProvider<Book> provider =
                specificationProviderManager.getSpecificationProvider(key);
        return provider.getSpecification(params);
    }
}
