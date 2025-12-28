package com.neo.tx.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Properties {
    @Value("${properties.score.min}")
    public int scoreMin;

    @Value("${properties.score.max}")
    public int scoreMax;

    @Value("${properties.risk.threshold.low}")
    public int riskThresholdLow;

    @Value("${properties.risk.threshold.medium}")
    public int riskThresholdMedium;

    @Value("${properties.risk.threshold.high}")
    public int riskThresholdHigh;

    @Value("${properties.kafka.producer.enabled}")
    public boolean kafkaProducerEnabled;

    @Value("${properties.kafka.consumer.enabled}")
    public boolean kafkaConsumerEnabled;

    @Value("${properties.kafka.consumer.validation.topic}")
    public String kafkaConsumerValidationTopic;
}