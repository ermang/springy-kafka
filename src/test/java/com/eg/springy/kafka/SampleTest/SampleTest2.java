package com.eg.springy.kafka.SampleTest;

import org.junit.jupiter.api.Test;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.ByteArraySerializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class SampleTest2 {

    @Test
    public void feedToTopic() throws IOException, ExecutionException, InterruptedException {
        // Define the Avro schema
        String schemaString = "{\"type\":\"record\",\"name\":\"User\",\"fields\":[{\"name\":\"id\",\"type\":\"string\"},{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"email\",\"type\":\"string\"}]}";
        Schema schema = new Schema.Parser().parse(schemaString);

        // Create a GenericRecord
        GenericRecord user = new GenericData.Record(schema);
        user.put("id", "1");
        user.put("name", "John Doe");
        user.put("email", "john.doe@example.com");

        // Serialize the GenericRecord to a byte array
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        DatumWriter<GenericRecord> datumWriter = new SpecificDatumWriter<>(schema);
//        Encoder encoder = EncoderFactory.get().binaryEncoder(byteArrayOutputStream, null);
//        datumWriter.write(user, encoder);
//        encoder.flush();

        //byte[] avroBytes = byteArrayOutputStream.toByteArray();

        // Send the serialized data to Kafka
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("schema.registry.url", "http://localhost:8081");

        props.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer"); // Ensure this is set for Avro


        try (KafkaProducer<String, GenericRecord> producer = new KafkaProducer<>(props)) {
            ProducerRecord<String, GenericRecord> record = new ProducerRecord<>("my-topic", user);
            RecordMetadata metadata = producer.send(record).get();
            System.out.printf("Sent record (key: %s, value: %s) " +
                    "meta(partition=%d, offset=%d)%n", null, record.value(), metadata.partition(), metadata.offset());
        }
    }
}
