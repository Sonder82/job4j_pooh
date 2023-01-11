package ru.job4j.pooh;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueService implements Service {

    private final ConcurrentHashMap<String, ConcurrentLinkedQueue<String>> queue = new ConcurrentHashMap<>();

    @Override
    public Resp process(Req req) {
        Resp resp = new Resp(TextAnswer.EMPTY_STRING.getText(), HttpCodes.NO_CONTENT.getCode());
        String name = req.getSourceName();
        if (HttpStatus.POST.getName().equals(req.getHttpRequestType())) {
            queue.putIfAbsent(name, new ConcurrentLinkedQueue<>());
            queue.get(name).add(req.getParam());
            resp = new Resp(req.getParam(), HttpCodes.NO_CONTENT.getCode());
        }
        if (HttpStatus.GET.getName().equals(req.getHttpRequestType())) {
            String text = queue.getOrDefault(name, new ConcurrentLinkedQueue<>()).poll();
            resp = new Resp(text, HttpCodes.OK.getCode());
        }
        return resp;
    }
}
