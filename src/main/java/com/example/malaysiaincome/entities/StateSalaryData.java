package com.example.malaysiaincome.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StateSalaryData {
    @JsonProperty("date") // Ensure the "date" field in JSON maps to this Java field
    private String date;

    @JsonProperty("state") // Ensure the "state" field in JSON maps to this Java field
    private String state;

    @JsonProperty("income_mean") // Ensure the "income_mean" field in JSON maps to this Java field
    private double incomeMean;

    @JsonProperty("income_median") // Ensure the "income_median" field in JSON maps to this Java field
    private double incomeMedian;
}
