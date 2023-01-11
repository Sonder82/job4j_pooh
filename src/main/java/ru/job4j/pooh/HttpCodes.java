package ru.job4j.pooh;

public enum HttpCodes {
    OK("200"),
    NO_CONTENT("204");

    private final String code;

    HttpCodes(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
