package com.neo.tx.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ValidationResultDto {
    public String code;
    public String description;
    public int scoreDelta;

    public ValidationResultDto() {

    }

    public ValidationResultDto(String code, String description, int scoreDelta) {
        this.code = code;
        this.description = description;
        this.scoreDelta = scoreDelta;
    }

    @Override
    public String toString() {
        return "ValidationResultDto{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", scoreDelta=" + scoreDelta +
                '}';
    }
}