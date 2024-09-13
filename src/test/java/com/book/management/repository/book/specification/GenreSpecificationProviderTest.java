package com.book.management.repository.book.specification;

import static com.book.management.util.TestConstants.GENRE_PARAM_NAME;
import static com.book.management.util.TestConstants.VALID_BOOK_GENRE_POETRY;
import static com.book.management.util.TestConstants.VALID_GENRE_IN_DIFFERENT_CASES_POETRY;
import static com.book.management.util.TestConstants.VALID_GENRE_SEARCH_PREDICATE;
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

@DisplayName("Genre Specification Provider Tests")
class GenreSpecificationProviderTest {
    private GenreSpecificationProvider genreSpecificationProvider;

    @BeforeEach
    void setUp() {
        genreSpecificationProvider = new GenreSpecificationProvider();
    }

    @Test
    @DisplayName("Get key -> (valid key)")
    void getKey_ValidKey_ReturnsCorrectKey() {
        String key = genreSpecificationProvider.getKey();
        assertEquals(GENRE_PARAM_NAME, key);
    }

    @Test
    @DisplayName("Build specification -> (valid genre parameter)")
    void getSpecification_ValidAuthor_ReturnsSpecification() {
        BookSearchParametersDto params = new BookSearchParametersDto(
                null, null, VALID_BOOK_GENRE_POETRY);

        Specification<Book> specification = genreSpecificationProvider.getSpecification(params);

        assertNotNull(specification);
    }

    @Test
    @DisplayName("Build specification -> (genre parameter in different case)")
    void getSpecification_AuthorParameterWithDifferentCase_ReturnsSpecification() {
        BookSearchParametersDto params =
                new BookSearchParametersDto(null, null, VALID_GENRE_IN_DIFFERENT_CASES_POETRY);

        Specification<Book> specification = genreSpecificationProvider.getSpecification(params);

        assertNotNull(specification);
    }

    @Test
    @DisplayName("Build specification -> (null parameters)")
    void getSpecification_NullAuthorParameter_ReturnsSpecification() {
        BookSearchParametersDto params =
                new BookSearchParametersDto(null, null, null);

        Specification<Book> specification = genreSpecificationProvider.getSpecification(params);

        assertNotNull(specification);
    }

    @Test
    @DisplayName("Build specification -> (genre parameter with correct case)")
    void getSpecification_ValidAuthor_ReturnsCorrectSpecification() {
        BookSearchParametersDto params =
                new BookSearchParametersDto(null, null, VALID_BOOK_GENRE_POETRY);

        CriteriaBuilder criteriaBuilder = mock(CriteriaBuilder.class);
        Root root = mock(Root.class);
        Path path = mock(Path.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        Predicate predicate = mock(Predicate.class);

        when(root.get(genreSpecificationProvider.getKey())).thenReturn(path);
        when(criteriaBuilder.lower(path)).thenReturn(path);
        when(criteriaBuilder.like(path, VALID_GENRE_SEARCH_PREDICATE)).thenReturn(predicate);

        Specification<Book> specification = genreSpecificationProvider.getSpecification(params);

        Predicate result = specification.toPredicate(root, query, criteriaBuilder);

        verify(criteriaBuilder).lower(path);
        verify(criteriaBuilder).like(path, VALID_GENRE_SEARCH_PREDICATE);
        assertEquals(predicate, result);
    }
}
