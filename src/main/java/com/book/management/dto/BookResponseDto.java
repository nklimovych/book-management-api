package com.book.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class BookResponseDto {
    @Schema(description = "Unique identifier of the book", example = "1")
    private Long id;

    @Schema(description = "Title of the book", example = "Kobzar")
    private String title;

    @Schema(description = "Author of the book", example = "Taras Shevchenko")
    private String author;

    @Schema(description = "Year the book was published", example = "1840")
    private int publicationYear;

    @Schema(description = "Genre of the book. Can be null if the genre is not specified",
            example = "Poetry", nullable = true)
    private String genre;

    @Schema(description = "International Standard Book Number", example = "978-966-1650-77-9")
    private String isbn;
}
