package com.book.management.repository.book.specification;

import com.book.management.dto.BookSearchParametersDto;
import com.book.management.model.Book;
import com.book.management.repository.SpecificationProvider;
import com.book.management.repository.book.BookSearchParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class GenreSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return BookSearchParameter.GENRE.getKey();
    }

    @Override
    public Specification<Book> getSpecification(BookSearchParametersDto params) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(root.get(getKey())),
                "%" + params.genre().toLowerCase() + "%"
        );
    }
}
