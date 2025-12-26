package com.neo.tx.repository.specification;

import com.neo.tx.model.OutboxEvent;
import com.neo.tx.model.Validation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EventOutboxJpaRepository extends JpaRepository<OutboxEvent, Long>, JpaSpecificationExecutor<OutboxEvent> {

}