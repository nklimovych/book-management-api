package com.book.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record BookSearchParametersDto(
        @Schema(description = "Title of the book to search for", example = "Kobzar")
        String title,

        @Schema(description = "Author of the book to search for", example = "Taras Shevchenko")
        String author,

        @Schema(description = "Genre of the book to search for", example = "Poetry")
        String genre
) {
}
