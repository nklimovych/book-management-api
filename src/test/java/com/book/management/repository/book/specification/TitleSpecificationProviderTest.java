package com.book.management.repository.book.specification;

import static com.book.management.util.TestConstants.TITLE_PARAM_NAME;
import static com.book.management.util.TestConstants.VALID_BOOK_TITLE;
import static com.book.management.util.TestConstants.VALID_TITLE_IN_DIFFERENT_CASES;
import static com.book.management.util.TestConstants.VALID_TITLE_SEARCH_PREDICATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.book.management.dto.BookSearchParametersDto;
import com.book.management.model.Book;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.jpa.domain.Specification;

@DisplayName("Title Specification Provider Tests")
class TitleSpecificationProviderTest {
    private TitleSpecificationProvider titleSpecificationProvider;

    @BeforeEach
    void setUp() {
        titleSpecificationProvider = new TitleSpecificationProvider();
    }

    @Test
    @DisplayName("Get key -> (valid key)")
    void getKey_ValidKey_ReturnsCorrectKey() {
        String key = titleSpecificationProvider.getKey();
        assertEquals(TITLE_PARAM_NAME, key);
    }

    @Test
    @DisplayName("Build specification -> (valid title parameter)")
    void getSpecification_ValidAuthor_ReturnsSpecification() {
        BookSearchParametersDto params = new BookSearchParametersDto(
                VALID_BOOK_TITLE, null, null);

        Specification<Book> specification = titleSpecificationProvider.getSpecification(params);

        assertNotNull(specification);
    }

    @Test
    @DisplayName("Build specification -> (title parameter in different case)")
    void getSpecification_AuthorParameterWithDifferentCase_ReturnsSpecification() {
        BookSearchParametersDto params =
                new BookSearchParametersDto(VALID_TITLE_IN_DIFFERENT_CASES, null, null);

        Specification<Book> specification = titleSpecificationProvider.getSpecification(params);

        assertNotNull(specification);
    }

    @Test
    @DisplayName("Build specification -> (null parameters)")
    void getSpecification_NullAuthorParameter_ReturnsSpecification() {
        BookSearchParametersDto params =
                new BookSearchParametersDto(null, null, null);

        Specification<Book> specification = titleSpecificationProvider.getSpecification(params);

        assertNotNull(specification);
    }

    @Test
    @DisplayName("Build specification -> (title parameter with correct case)")
    void getSpecification_ValidAuthor_ReturnsCorrectSpecification() {
        BookSearchParametersDto params =
                new BookSearchParametersDto(VALID_BOOK_TITLE, null, null);

        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        Root root = mock(Root.class);
        Path path = mock(Path.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        Predicate predicate = mock(Predicate.class);

        when(root.get(titleSpecificationProvider.getKey())).thenReturn(path);
        when(criteriaBuilder.lower(path)).thenReturn(path);
        when(criteriaBuilder.like(path, VALID_TITLE_SEARCH_PREDICATE)).thenReturn(predicate);

        Specification<Book> specification = titleSpecificationProvider.getSpecification(params);

        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).lower(path);
        verify(criteriaBuilder).like(path, VALID_TITLE_SEARCH_PREDICATE);
        assertEquals(predicate, result);
    }
}
