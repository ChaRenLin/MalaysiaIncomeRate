package com.example.malaysiaincome.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.malaysiaincome.entities.*;
import com.example.malaysiaincome.config.Constants;
import com.example.malaysiaincome.respositories.*;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;


import java.util.*;

@Service
public class SalaryService {

    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(SalaryService.class);

    public SalaryService(RestTemplate restTemplate, UserRepository userRepository) {
        this.restTemplate = restTemplate;
        this.userRepository = userRepository;
    }

    public String getExternalSalaryData() {
        logger.info(Constants.RESPONSE_LOGGER + "[ Fetching data from Malaysia Salary API ]");

        // Fetch state salary data (no filtering by year)
        List<StateSalaryData> stateSalaryData = restTemplate.exchange(
                Constants.MALAYSIA_STATE_SALARY_API,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<StateSalaryData>>() {}  // List of StateSalaryData
        ).getBody();

        // Check if data is empty
        if (stateSalaryData == null || stateSalaryData.isEmpty()) {
            logger.warn(Constants.RESPONSE_LOGGER + "No salary data found.");
            return "No salary data found.";
        }

        // Fetch country salary data (for overall comparison)
        List<SalaryData> countrySalaryData = restTemplate.exchange(
                Constants.MALAYSIA_SALARY_API,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<SalaryData>>() {}  // List of SalaryData
        ).getBody();

        // Check if country salary data is empty
        if (countrySalaryData == null) {
            logger.warn(Constants.RESPONSE_LOGGER + "No country salary data found.");
            return "No country salary data found.";
        }

        // Process the salary data
        return processSalaryData(stateSalaryData, countrySalaryData);
    }

    // Method to process the salary data and return structured response
    private String processSalaryData(List<StateSalaryData> stateSalaryData, List<SalaryData> countrySalaryData) {
        List<UserVO> dbUsers = userRepository.findAll();  // Get all users (or query specific if needed)
        Map<String, List<Double>> salaryGroups = new HashMap<>();
        
        // Group the salaries by age range
        for (UserVO user : dbUsers) {
            int age = user.getAge();
            double salary = user.getSalary();
            
            // Determine the age group and add the salary to the appropriate group
            String ageGroup = getAgeGroup(age);
            salaryGroups.computeIfAbsent(ageGroup, k -> new ArrayList<>()).add(salary);
        }
    
        // To store the formatted result
        StringBuilder result = new StringBuilder();
    
        // Process state-level salary data and group them by year
        Map<String, StringBuilder> stateResults = new HashMap<>();
        for (StateSalaryData stateData : stateSalaryData) {
            String year = stateData.getDate(); // Get year from date
            stateResults.putIfAbsent(year, new StringBuilder());
    
            stateResults.get(year).append("[State: ")
                    .append(stateData.getState())
                    .append(" : ");
    
            // Process each age group and compare with state-level mean
            salaryGroups.forEach((ageGroup, salaries) -> {
                double ageGroupAverage = salaries.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                String comparison = ageGroupAverage > stateData.getIncomeMean() ? Constants.ABOVE_AVERAGE : Constants.BELOW_AVERAGE;
                stateResults.get(year).append("[").append(ageGroup).append(" : ").append(comparison).append("] ");
            });
    
            stateResults.get(year).append("], ");
        }
    
        // Append the results for each state
        stateResults.forEach((year, stateResult) -> {
            result.append(Constants.SLT_YEAR).append(year).append(" : ")
                  .append(stateResult.toString().replaceAll(", $", ""))
                  .append("\n");
        });
    
        // Process country salary data for comparison (similar to the state data)
        result.append(Constants.CTRY_LVL_SLR_COMP);
        Map<String, StringBuilder> countryResults = new HashMap<>();
        for (SalaryData countryData : countrySalaryData) {
            String year = countryData.getDate(); // Get year from date
            countryResults.putIfAbsent(year, new StringBuilder());
    
            countryResults.get(year).append(Constants.SLT_YEAR).append(year).append(" : ");
    
            // Compare each age group with country-level data
            salaryGroups.forEach((ageGroup, salaries) -> {
                double ageGroupAverage = salaries.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                String comparison = ageGroupAverage > countryData.getIncomeMean() ? Constants.ABOVE_AVERAGE : Constants.BELOW_AVERAGE;
                countryResults.get(year).append("[").append(ageGroup).append(" : ").append(comparison).append("] ");
            });
    
            countryResults.get(year).append("\n");
        }
    
        // Append the results for each country-level comparison
        countryResults.forEach((year, countryResult) -> {
            result.append(countryResult.toString());
        });
    
        return result.toString();
    }
    
    

    // Helper method to determine age group
    private String getAgeGroup(int age) {
        if (age <= 30) {
            return "0-30";
        } else if (age <= 50) {
            return "31-50";
        } else if (age <= 70) {
            return "51-70";
        } else {
            return "71+";
        }
    }
}
