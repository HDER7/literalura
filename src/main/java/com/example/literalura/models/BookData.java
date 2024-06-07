package com.example.literalura.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BookData(@JsonAlias("id") Long id,
                       @JsonAlias("title")String title,
                       @JsonAlias("authors") List<AuthorData> author,
                       @JsonAlias("languages")List<String> language,
                       @JsonAlias("download_count")Long downloads) {
}
