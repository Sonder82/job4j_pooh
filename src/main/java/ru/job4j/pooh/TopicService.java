package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    public static final String GET = "GET";
    public static final String POST = "POST";
    private final ConcurrentHashMap<String,
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topics = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp result = null;
        String name = req.getSourceName();
        if (GET.equals(req.getHttpRequestType())) {
            topics.putIfAbsent(name, new ConcurrentHashMap<>());
            topics.get(req.getSourceName()).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>());
            result = new Resp(
                    topics.get(name).getOrDefault(req.getParam(), new ConcurrentLinkedQueue<>()).poll(), "200");
        }
        if (POST.equals(req.getHttpRequestType())) {
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> map = topics.get(name);
            for (String key : map.keySet()) {
                map.get(key).add(req.getParam());
            }
            result = new Resp(req.getParam(), "200");
        }
        return result;
    }
}

