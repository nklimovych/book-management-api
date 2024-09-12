package com.book.management.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record BookSearchParametersDto(
        @Schema(example = "Kobzar")
        String title,

        @Schema(example = "Taras Shevchenko")
        String author,

        @Schema(example = "Poetry")
        String genre
) {
}
