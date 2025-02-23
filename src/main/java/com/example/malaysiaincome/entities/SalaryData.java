package com.example.malaysiaincome.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
public class SalaryData {
    @JsonProperty("date")
    private String date;

    @JsonProperty("state")
    private String state;

    @JsonProperty("income_mean")
    private double incomeMean;

    @JsonProperty("income_median")
    private double incomeMedian;

    // Getters and setters
}

