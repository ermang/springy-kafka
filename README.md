# something

docker run --rm --net=host --name=schema-registry -e SCHEMA_REGISTRY_KAFKASTORE_BOOTSTRAP_SERVERS=localhost:9092 -e SCHEMA_REGISTRY_HOST_NAME=localhost -e SCHEMA_REGISTRY_LISTENERS=http://localhost:8081  -p 8081:8081  confluentinc/cp-schema-registry:latest

docker run -d -p 9092:9092 --name kafka-broker apache/kafka:3.8.0

access the registry with http://localhost:8081/subjects from web browser


for schema registry configuration with docker  
https://docs.confluent.io/platform/current/installation/docker/config-reference.html#sr-long-configuration



