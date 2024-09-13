package com.book.management.repository.book;

import static com.book.management.util.TestConstants.AUTHOR_PARAM_NAME;
import static com.book.management.util.TestConstants.GENRE_PARAM_NAME;
import static com.book.management.util.TestConstants.TITLE_PARAM_NAME;
import static com.book.management.util.TestConstants.VALID_BOOK_AUTHOR;
import static com.book.management.util.TestConstants.VALID_BOOK_GENRE_POETRY;
import static com.book.management.util.TestConstants.VALID_BOOK_TITLE;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import com.book.management.dto.BookSearchParametersDto;
import com.book.management.model.Book;
import com.book.management.repository.SpecificationProvider;
import com.book.management.repository.SpecificationProviderManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

@DisplayName("Book Specification Builder Tests")
class BookSpecificationBuilderTest {

    @Mock
    private SpecificationProviderManager<Book> specificationProviderManager;

    @Mock
    private SpecificationProvider<Book> titleSpecificationProvider;

    @Mock
    private SpecificationProvider<Book> authorSpecificationProvider;

    @Mock
    private SpecificationProvider<Book> genreSpecificationProvider;

    @InjectMocks
    private BookSpecificationBuilder specificationBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(specificationProviderManager.getSpecificationProvider(TITLE_PARAM_NAME))
                .thenReturn(titleSpecificationProvider);
        when(specificationProviderManager.getSpecificationProvider(AUTHOR_PARAM_NAME))
                .thenReturn(authorSpecificationProvider);
        when(specificationProviderManager.getSpecificationProvider(GENRE_PARAM_NAME))
                .thenReturn(genreSpecificationProvider);
    }

    @Test
    @DisplayName("Build specification -> (title only)")
    void build_WithTitleParameter_ReturnsTitleSpecification() {
        BookSearchParametersDto params = new BookSearchParametersDto(VALID_BOOK_TITLE, null, null);

        when(titleSpecificationProvider.getSpecification(params))
                .thenReturn((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());

        Specification<Book> specification = specificationBuilder.build(params);

        assertNotNull(specification);
    }

    @Test
    @DisplayName("Build specification -> (author only)")
    void build_WithAuthorParameter_ReturnsAuthorSpecification() {
        BookSearchParametersDto params =
                new BookSearchParametersDto(null, VALID_BOOK_AUTHOR, null);

        when(authorSpecificationProvider.getSpecification(params))
                .thenReturn((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());

        Specification<Book> specification = specificationBuilder.build(params);

        assertNotNull(specification);
    }

    @Test
    @DisplayName("Build specification -> (genre only)")
    void build_WithGenreParameter_ReturnsGenreSpecification() {
        BookSearchParametersDto params =
                new BookSearchParametersDto(null, null, VALID_BOOK_GENRE_POETRY);

        when(genreSpecificationProvider.getSpecification(params))
                .thenReturn((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());

        Specification<Book> specification = specificationBuilder.build(params);

        assertNotNull(specification);
    }

    @Test
    @DisplayName("Build specification -> (all parameters)")
    void build_WithAllParameters_ReturnsCombinedSpecification() {
        BookSearchParametersDto params = new BookSearchParametersDto(
                VALID_BOOK_TITLE, VALID_BOOK_AUTHOR, VALID_BOOK_GENRE_POETRY);

        when(titleSpecificationProvider.getSpecification(params))
                .thenReturn((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());
        when(authorSpecificationProvider.getSpecification(params))
                .thenReturn((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());
        when(genreSpecificationProvider.getSpecification(params))
                .thenReturn((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());

        Specification<Book> specification = specificationBuilder.build(params);

        assertNotNull(specification);
    }

    @Test
    @DisplayName("Build specification -> (no parameters)")
    void build_WithNoParameters_ReturnsEmptySpecification() {
        BookSearchParametersDto params = new BookSearchParametersDto(null, null, null);

        Specification<Book> specification = specificationBuilder.build(params);

        assertNull(specification.toPredicate(null, null, null));
    }
}
