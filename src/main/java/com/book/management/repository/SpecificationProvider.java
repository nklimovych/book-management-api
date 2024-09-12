package com.book.management.repository;

import com.book.management.dto.BookSearchParametersDto;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationProvider<T> {
    String getKey();

    Specification<T> getSpecification(BookSearchParametersDto params);
}
