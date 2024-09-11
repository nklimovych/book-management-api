package com.book.management.dto;

import com.book.management.validator.ValidYearRange;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;

@Data
public class CreateBookRequestDto {
    @NotBlank
    private String title;

    @NotBlank
    private String author;

    @ValidYearRange(message = "Publication year can not be greater than the current year")
    private int publicationYear;

    private String genre;

    @NotNull
    @ISBN(type = ISBN.Type.ISBN_13)
    private String isbn;
}
