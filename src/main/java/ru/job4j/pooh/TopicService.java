package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TopicService implements Service {

    private final ConcurrentHashMap<String,
            ConcurrentHashMap<String, ConcurrentLinkedQueue<String>>> topics = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp result = new Resp(TextAnswer.EMPTY_STRING.getText(), HttpCodes.NO_CONTENT.getCode());
        String name = req.getSourceName();
        if (HttpStatus.GET.getName().equals(req.getHttpRequestType())) {
            topics.putIfAbsent(name, new ConcurrentHashMap<>());
            result = (!checkGet(req)) ? new Resp(topics.get(name)
                    .getOrDefault(req.getParam(), new ConcurrentLinkedQueue<>()).poll(), HttpCodes.OK.getCode())
                    : new Resp(TextAnswer.EMPTY_STRING.getText(), HttpCodes.OK.getCode());
        }
        if (HttpStatus.POST.getName().equals(req.getHttpRequestType())) {
            if (!checkPost(req)) {
                for (String key : topics.get(name).keySet()) {
                    topics.get(name).get(key).add(req.getParam());
                }
                result = new Resp(req.getParam(), HttpCodes.NO_CONTENT.getCode());
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

