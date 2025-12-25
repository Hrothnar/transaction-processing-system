package com.neo.tx.message_broker;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "properties.kafka.consumer", name = "enabled", havingValue = "true")
public class Consumer {

    private static final Logger log = LoggerFactory.getLogger(Consumer.class);

    @KafkaListener(topics = "${properties.kafka.consumer.validation.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void onMessage(ConsumerRecord<String, String> record) {
        String formatted = String.format("\n\n--- Kafka message received ---\n" +
                        "topic={%s} partition={%d} offset={%d}\n" +
                        "key={%s}\n" +
                        "value={%s}\n" +
                        "------------------------------\n",
                record.topic(), record.partition(), record.offset(), record.key(), record.value());

        log.info(formatted);
    }
}