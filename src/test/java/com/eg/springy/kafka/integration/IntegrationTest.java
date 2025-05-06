package com.eg.springy.kafka.integration;


import com.eg.springy.kafka.misc.NextHandler;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.EmbeddedKafkaKraftBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;
import java.util.Map;

@ActiveProfiles("test")
@SpringBootTest
public class IntegrationTest {

    private static EmbeddedKafkaBroker kafkaBroker;

    @Autowired
    KafkaTemplate kafkaTemplate;
    @MockitoBean
    NextHandler nextHandler;


    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        kafkaBroker = new EmbeddedKafkaKraftBroker(1, 1, "my-topic", "out-topic");
        kafkaBroker.afterPropertiesSet();
        String brokerAddress = kafkaBroker.getBrokersAsString();

        registry.add("spring.kafka.bootstrap-servers", () -> brokerAddress);
    }

    @Test
    public void send_dummy_kafka_message_and_receive() {
        kafkaTemplate.send("my-topic", "lolo");

        Mockito.verify(nextHandler, Mockito.timeout(10000).times(1)).handle("lolo");
    }

    @Test
    public void throw_error_during_message_handling_move_message_to_dlt() {

        Mockito.doThrow(new RuntimeException())
                        .when(nextHandler)
                                .handle(Mockito.anyString());

        kafkaTemplate.send("my-topic", "lolo");

        // Use KafkaTestUtils to get a consumer
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "false", kafkaBroker);
        Consumer<String, String> consumer = new DefaultKafkaConsumerFactory(consumerProps).createConsumer();

        // Subscribe to the "my-topic-dlt" topic (DLQ)
        consumer.subscribe(Collections.singletonList("my-topic-dlt"));

        // Consume a single message from the DLQ topic
        ConsumerRecord<String, String> record =
                KafkaTestUtils.getSingleRecord(consumer, "my-topic-dlt");

        Assertions.assertEquals("lolo", record.value());
    }


}
