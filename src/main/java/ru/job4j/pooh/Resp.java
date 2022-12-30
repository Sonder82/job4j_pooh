package ru.job4j.pooh;

/**
 * Класс выполняет работу связанную с ответом от сервера.
 */
public class Resp {
    /**
     * Поле текст ответа
     */
    private final String text;
    /**
     * Поле код статуса ответа HTTP
     */
    private final String status;

    public Resp(String text, String status) {
        this.text = text;
        this.status = status;
    }

    public String text() {
        return text;
    }

    public String status() {
        return status;
    }
}
