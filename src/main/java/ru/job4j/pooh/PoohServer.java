package ru.job4j.pooh;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PoohServer {

    private final HashMap<String, Service> modes = new HashMap<>();

    public void start() {
        modes.put(TextAnswer.QUEUE.getText(), new QueueService());
        modes.put(TextAnswer.TOPIC.getText(), new TopicService());
        ExecutorService pool = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );
        try (ServerSocket server = new ServerSocket(9000)) {
            while (!server.isClosed()) {
                Socket socket = server.accept();
                pool.execute(() -> {
                    try (OutputStream out = socket.getOutputStream();
                         InputStream input = socket.getInputStream()) {
                        byte[] buff = new byte[1_000_000];
                        int total = input.read(buff);
                        String content = new String(Arrays.copyOfRange(buff, 0, total), StandardCharsets.UTF_8);
                        Req req = Req.of(content);
                        Service service = modes.get(req.getPoohMode());
                        Resp resp = service.process(req);
                        String ls = System.lineSeparator();
                        out.write(("HTTP/1.1 " + resp.status() + ls + ls).getBytes());
                        out.write((resp.text().concat(ls)).getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new PoohServer().start();
    }
}
