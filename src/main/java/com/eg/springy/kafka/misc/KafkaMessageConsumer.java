package com.eg.springy.kafka.misc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class KafkaMessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageConsumer.class);
    private final NextHandler nextHandler;
    private final KafkaTemplate kafkaTemplate;

    public KafkaMessageConsumer(NextHandler nextHandler, KafkaTemplate kafkaTemplate) {
        this.nextHandler = nextHandler;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "${custom.topic}", groupId = "${custom.group}")
    public void listen(String message, Acknowledgment ack) {

        try {
            LOGGER.info(message);
            nextHandler.handle(message);
            ack.acknowledge();
        } catch (RuntimeException e) {
            LOGGER.error("OOPS moving to DLQ", e);
            //ack.nack(Duration.ofSeconds(5));
            kafkaTemplate.send("my-topic-dlt", message);

            ack.acknowledge();
        }
    }
}
