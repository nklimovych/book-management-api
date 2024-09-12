package com.book.management.dto;

import com.book.management.validator.ValidYearRange;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;

@Data
public class CreateBookRequestDto {
    @NotBlank
    @Schema(example = "Kobzar")
    private String title;

    @NotBlank
    @Schema(example = "Taras Shevchenko")
    private String author;

    @ValidYearRange(message = "Publication year can not be greater than the current year")
    @Schema(example = "1840")
    private int publicationYear;

    @Schema(example = "Poetry", nullable = true)
    private String genre;

    @NotNull
    @ISBN(type = ISBN.Type.ISBN_13)
    @Schema(example = "978-966-1650-77-9")
    private String isbn;
}
