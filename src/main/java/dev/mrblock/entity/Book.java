package dev.mrblock.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    long id;
    String title;
    String author;
    String isbn;

    @JsonProperty(value = "publication_year")
    Integer publicationYear;
    String genre;

    public enum Genre {

        FICTION,
        NON_FICTION,
        SCIENCE,
        HISTORY,
        OTHER

    }

}