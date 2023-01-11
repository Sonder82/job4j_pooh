package ru.job4j.pooh;

public enum HttpStatus {
    POST("POST"),
    GET("GET");

    private final String name;

    HttpStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
