package ru.job4j.pooh;

/**
 * Класс служит для парсинга входящего запроса.
 */
public class Req {

    /**
     * Поле указывает на тип запроса(GET или POST)
     */
    private final String httpRequestType;
    /**
     * Поле указывает на режим работы(queue или topic)
     */
    private final String poohMode;
    /**
     * Поле указывает на имя очереди или топика
     */
    private final String sourceName;
    /**
     * Поле содержимое запроса
     */
    private final String param;

    public Req(String httpRequestType, String poohMode, String sourceName, String param) {
        this.httpRequestType = httpRequestType;
        this.poohMode = poohMode;
        this.sourceName = sourceName;
        this.param = param;
    }

    /**
     * Метод выполняет парсинг входящего запроса
     * @param context текст запроса
     * @return объект класса {@link Req}
     */
    public static Req of(String context) {
        String[] splitBy = context.split("/");
        String httpRequestType = splitBy[0].trim();
        String poohMode = splitBy[1];
        String sourceName = splitBy[2].split(" ")[0];
        String[] row = context.split(System.lineSeparator());
        String param = null;
        if (poohMode.equals("queue")) {
             param = httpRequestType.equals("POST") ? row[row.length - 1] : "";
        }
        if (poohMode.equals("topic")) {
            param = httpRequestType.equals("POST") ? row[row.length - 1] : splitBy[3].split(" ")[0];
        }
        return new Req(httpRequestType, poohMode, sourceName, param);
    }

    public String getHttpRequestType() {
        return httpRequestType;
    }

    public String getPoohMode() {
        return poohMode;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getParam() {
        return param;
    }
}
