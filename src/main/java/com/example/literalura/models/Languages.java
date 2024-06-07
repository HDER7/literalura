package com.example.literalura.models;

public enum Languages {
    INGLES("en", "Inglés"),
    ESPANOL("es", "Español"),
    FRANCES("fr", "Francés"),
    PORTUGUES("pt", "Portugués"),
    TAGALOG("tl", "Tagalog");

    private final String abreviature;
    private final String displayName;

    Languages(String abreviature, String displayName) {
        this.abreviature = abreviature;
        this.displayName = displayName;
    }

    public String getAbreviature() {
        return abreviature;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Languages fromString(String text) {
        for (Languages language : Languages.values()) {
            if (language.abreviature.equalsIgnoreCase(text)) {
                return language;
            }
        }
        throw new IllegalArgumentException("No hay lenguaje con abreviatura: " + text);
    }
}

