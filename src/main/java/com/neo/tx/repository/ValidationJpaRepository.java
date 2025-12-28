package com.neo.tx.repository;

import com.neo.tx.model.Validation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ValidationJpaRepository extends JpaRepository<Validation, Long>, JpaSpecificationExecutor<Validation> {

}
