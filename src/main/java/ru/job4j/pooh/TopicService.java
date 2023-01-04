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
        Resp result = new Resp("", "204");
        String name = req.getSourceName();
        if (GET.equals(req.getHttpRequestType())) {
            topics.putIfAbsent(name, new ConcurrentHashMap<>());
            if (!checkGet(req)) {
                result = new Resp(
                        topics.get(name)
                                .getOrDefault(req.getParam(), new ConcurrentLinkedQueue<>()).poll(), "200");
            }
        }
        if (POST.equals(req.getHttpRequestType())) {
            if (!checkPost(req)) {
                for (String key : topics.get(name).keySet()) {
                    topics.get(name).get(key).add(req.getParam());
                }
                result = new Resp(req.getParam(), "200");
            }
        }
        return result;
    }

    private boolean checkPost(Req req) {
       return topics.get(req.getSourceName()) == null;
    }

    private boolean checkGet(Req req) {
        return topics.get(req.getSourceName()).putIfAbsent(req.getParam(), new ConcurrentLinkedQueue<>()) == null;
    }
}

