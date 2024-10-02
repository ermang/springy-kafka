package com.eg.springy.kafka.SampleTest;

import org.apache.avro.Schema;
import org.apache.avro.SchemaParseException;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SampleTest {

    @Test
    public void doIt() throws IOException {
        String schemaFilePath = "user.avsc";
        String outputFilePath = "user.avro";


      InputStream schemaInputStream = SampleTest.class.getClassLoader().getResourceAsStream(schemaFilePath);
            // Load schema
            Schema schema = new Schema.Parser().parse(schemaInputStream);

            // Create a record
            GenericRecord user = new GenericData.Record(schema);
            user.put("id", "1");
            user.put("name", "John Doe");
            user.put("email", "johndoe@example.com");

            // Serialize to binary file
            DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
            Encoder encoder = EncoderFactory.get().binaryEncoder(new FileOutputStream(outputFilePath), null);
            datumWriter.write(user, encoder);
            encoder.flush();
            System.out.println("Avro binary file created: " + outputFilePath);


    }
}
