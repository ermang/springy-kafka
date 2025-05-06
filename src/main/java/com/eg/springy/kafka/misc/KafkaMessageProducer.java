package com.eg.springy.kafka.misc;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageProducer {

private final KafkaTemplate kafkaTemplate;

    public KafkaMessageProducer(KafkaTemplate kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}
