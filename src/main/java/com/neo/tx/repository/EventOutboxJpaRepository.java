package com.neo.tx.repository;

import com.neo.tx.model.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EventOutboxJpaRepository extends JpaRepository<OutboxEvent, Long>, JpaSpecificationExecutor<OutboxEvent> {

}