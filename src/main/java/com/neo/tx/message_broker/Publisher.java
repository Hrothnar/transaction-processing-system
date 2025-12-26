package com.neo.tx.message_broker;

import com.neo.tx.model.OutboxEvent;
import com.neo.tx.repository.EventOutboxRepository;
import jakarta.transaction.Transactional;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class Publisher {

    private static final int FIXED_DELAY = 5000;
    private static final int CLAIM_BATCH_SIZE = 50;
    private static final int MAX_ATTEMPTS = 10;

    private final EventOutboxRepository eventOutboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public Publisher(EventOutboxRepository eventOutboxRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.eventOutboxRepository = eventOutboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = FIXED_DELAY)
    @Transactional
    public void publish() {
        List<OutboxEvent> eventList = eventOutboxRepository.claimBatch(CLAIM_BATCH_SIZE);

        for (OutboxEvent outboxEvent : eventList) {
            try {
                kafkaTemplate.send(outboxEvent.getTopic(), outboxEvent.getEventKey(), outboxEvent.getPayload()).get(); // block = simplest correctness
                outboxEvent.setStatus(OutboxEvent.Status.SENT);
                outboxEvent.setLastError(null);
            } catch (Exception ex) {
                int attempts = outboxEvent.getAttempts() + 1;
                outboxEvent.setAttempts(attempts);
                outboxEvent.setLastError(ex.getMessage());

                if (attempts >= MAX_ATTEMPTS) {
                    outboxEvent.setStatus(OutboxEvent.Status.FAILED);
                } else {
                    outboxEvent.setStatus(OutboxEvent.Status.NEW);
                    outboxEvent.setAvailableAt(Instant.now().plusSeconds((long) Math.min(60, Math.pow(2, attempts)))); // basic backoff logic
                }
            }

            eventOutboxRepository.save(outboxEvent);
        }
    }
}