package dev.mrblock.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    long id;
    String title;            // Длинна от 2 до 100
    String author;           // Длинна от 2 до 50
    String isbn;             // Формат (регулярное выражение): \d{3}-\d-\d{5}-\d{3}-\d

    @JsonProperty(value = "publication_year")
    Integer publicationYear; // В диапазоне от 2000 до 2026
    String genre;            // Должен быть enum

    public enum Genre {

        FICTION,
        NON_FICTION,
        SCIENCE,
        HISTORY,
        OTHER

    }

}