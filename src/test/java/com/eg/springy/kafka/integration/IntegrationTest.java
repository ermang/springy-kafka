package com.eg.springy.kafka.integration;


import com.eg.springy.kafka.misc.NextHandler;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.EmbeddedKafkaKraftBroker;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

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
}
