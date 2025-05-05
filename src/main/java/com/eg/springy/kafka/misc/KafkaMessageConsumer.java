package com.eg.springy.kafka.misc;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageConsumer {

    private final NextHandler nextHandler;

    public KafkaMessageConsumer(NextHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @KafkaListener(topics = "${custom.topic}", groupId = "${custom.group}")
    public void listen(String message) {
        System.out.println(message);
        nextHandler.handle(message);
    }
}
