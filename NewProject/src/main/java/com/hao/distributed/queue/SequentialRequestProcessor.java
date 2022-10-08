package com.hao.distributed.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 使用内存队列BlockingQueue实现请求的顺序性
 */
public class SequentialRequestProcessor {
    // 队列容量
    private static final int QUEUE_CAPACITY = 10;
    // 请求队列
    private BlockingQueue<Request> requestQueue;

    public static class Request {
        private final int id;

        public Request(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }

    public SequentialRequestProcessor() {
        this.requestQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        startProcessing();
    }

    private void startProcessing() {
        new Thread(() -> {
            while (true) {
                try {
                    // 从队列中获取请求,阻塞直到有请求可用
                    Request request = requestQueue.take();
                    processRequest(request);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }

    // 添加请求到队列
    public void addRequest(Request request) throws InterruptedException {
        requestQueue.add(request);
    }

    private void processRequest(Request request) {
        System.out.println("Processing request: " + request.getId());
        // 模拟处理时间
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Finished porcessing request: " + request.getId());
    }

    public static void main(String[] args) {
        SequentialRequestProcessor processor = new SequentialRequestProcessor();

        //模拟添加请求
        for (int i = 0; i < 5; i++) {
            final int requestId = i;
            new Thread(() -> {
                try {
                    processor.addRequest(new Request(requestId));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }

}
