package com.neo.tx.repository.specification;

import com.neo.tx.enums.Decision;
import com.neo.tx.enums.RiskLevel;
import com.neo.tx.model.Validation;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.UUID;

public final class ValidationSpecification {

    private ValidationSpecification() {

    }

    public static Specification<Validation> userId(UUID userId) {
        return (root, query, builder) ->
                builder.equal(root.get("transaction").get("userId"), userId);
    }

    public static Specification<Validation> riskLevel(RiskLevel riskLevel) {
        return (root, query, builder) ->
                builder.equal(root.get("riskLevel"), riskLevel);
    }

    public static Specification<Validation> decision(Decision decision) {
        return (root, query, builder) ->
                builder.equal(root.get("decision"), decision);
    }

    public static Specification<Validation> initializedFrom(Instant from) {
        return (root, query, builder) ->
                builder.greaterThanOrEqualTo(root.get("transaction").get("initialized"), from.toEpochMilli());
    }

    public static Specification<Validation> initializedTo(Instant to) {
        return (root, query, builder) ->
                builder.lessThanOrEqualTo(root.get("transaction").get("initialized"), to.toEpochMilli());
    }

    public static Specification<Validation> buildCondition(UUID userId, RiskLevel riskLevel, Decision decision, Instant from, Instant to) {
        Specification<Validation> specification = Specification.unrestricted();

        if (userId != null) specification = specification.and(ValidationSpecification.userId(userId));
        if (riskLevel != null) specification = specification.and(ValidationSpecification.riskLevel(riskLevel));
        if (decision != null) specification = specification.and(ValidationSpecification.decision(decision));
        if (from != null) specification = specification.and(ValidationSpecification.initializedFrom(from));
        if (to != null) specification = specification.and(ValidationSpecification.initializedTo(to));

        return specification;
    }
}