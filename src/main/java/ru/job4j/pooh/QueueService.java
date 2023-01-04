package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    public static final String GET = "GET";
    public static final String POST = "POST";
    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp resp = new Resp("", "204");
        String name = req.getSourceName();
        if (POST.equals(req.getHttpRequestType())) {
            queue.putIfAbsent(name, new ConcurrentLinkedQueue<>());
            queue.get(name).add(req.getParam());
            resp = new Resp(req.getParam(), "200");
        }
        if (GET.equals(req.getHttpRequestType())) {
            String text = queue.getOrDefault(name, new ConcurrentLinkedQueue<>()).poll();
            resp = new Resp(text, "200");
        }

        return resp;
    }
}
