package com.neo.tx.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "properties")
public class Properties {
    public int scoreMin;
    public int scoreMax;
    public int riskThresholdLow;
    public int riskThresholdMedium;
    public int riskThresholdHigh;

    public void setScoreMin(int scoreMin) {
        this.scoreMin = scoreMin;
    }

    public void setScoreMax(int scoreMax) {
        this.scoreMax = scoreMax;
    }

    public void setRiskThresholdLow(int riskThresholdLow) {
        this.riskThresholdLow = riskThresholdLow;
    }

    public void setRiskThresholdMedium(int riskThresholdMedium) {
        this.riskThresholdMedium = riskThresholdMedium;
    }

    public void setRiskThresholdHigh(int riskThresholdHigh) {
        this.riskThresholdHigh = riskThresholdHigh;
    }
}