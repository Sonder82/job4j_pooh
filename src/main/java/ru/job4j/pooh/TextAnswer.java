package ru.job4j.pooh;

public enum TextAnswer {
    QUEUE("queue"),
    TOPIC("topic"),
    EMPTY_STRING("");

    private final String text;

    TextAnswer(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
