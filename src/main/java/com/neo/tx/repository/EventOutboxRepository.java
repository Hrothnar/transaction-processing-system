package com.neo.tx.repository;

import com.neo.tx.model.OutboxEvent;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventOutboxRepository {

    private final EntityManager entityManager;

    public EventOutboxRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public List<OutboxEvent> claimBatch(int limit) {
        return entityManager.createNativeQuery("""
                        SELECT *
                        FROM event_outbox
                        WHERE status = 'NEW' AND available_at <= now()
                        ORDER BY id
                        FOR UPDATE SKIP LOCKED
                        LIMIT :limit
                        """, OutboxEvent.class)
                .setParameter("limit", limit)
                .getResultList();
    }

    @Transactional
    public OutboxEvent save(OutboxEvent outboxEvent) {
        if (outboxEvent.getId() == null) {
            entityManager.persist(outboxEvent);
        } else {
            outboxEvent = entityManager.merge(outboxEvent);
        }

        return outboxEvent;
    }
}