package com.eg.misc;

import com.example.kafka.User;
import org.apache.avro.generic.GenericRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageListener {

    @KafkaListener(topics = "my-topic", groupId = "my-group")
    public void listen(GenericRecord data) {
        System.out.println(data);
    }
}
