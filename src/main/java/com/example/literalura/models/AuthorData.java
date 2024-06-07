package com.example.literalura.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthorData(@JsonAlias("name") String name,
                         @JsonAlias("birth_year") Integer birth,
                         @JsonAlias("death_year") Integer dead) {
}
