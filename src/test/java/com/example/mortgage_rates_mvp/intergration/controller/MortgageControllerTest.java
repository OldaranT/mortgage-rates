package com.example.mortgage_rates_mvp.intergration.controller;

import com.example.mortgage_rates_mvp.dto.MortgageData;
import com.example.mortgage_rates_mvp.dto.MortgageRate;
import com.example.mortgage_rates_mvp.dto.MortgageReport;
import com.example.mortgage_rates_mvp.service.MortgageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MortgageControllerTest {


    private static final Timestamp CONSTANT_TIME_STAMP = Timestamp.valueOf(LocalDateTime
            .of(2025, 5, 21, 0, 0, 0));

    private final List<MortgageRate> mortgageRates = List.of(
            new MortgageRate(10, new BigDecimal("3.5"), CONSTANT_TIME_STAMP),
            new MortgageRate(15, new BigDecimal("4.0"), CONSTANT_TIME_STAMP),
            new MortgageRate(20, new BigDecimal("4.5"), CONSTANT_TIME_STAMP),
            new MortgageRate(25, new BigDecimal("5.0"), CONSTANT_TIME_STAMP),
            new MortgageRate(30, new BigDecimal("6.5"), CONSTANT_TIME_STAMP)
    );

    @Autowired
    private MortgageService mortgageService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testInterestRatesGet() throws Exception {
        mockMvc.perform(get("/api/interest-rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[3].maturityPeriod").value(25));
    }

    @ParameterizedTest
    @MethodSource("mortgageEvaluationCases")
    void testMortgageCheckPost(MortgageData mortgageData, MortgageReport mortgageReport) throws Exception {

        mockMvc.perform(post("/api/mortgage-check")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mortgageData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feasible").value(mortgageReport.isFeasible()))
                .andExpect(jsonPath("$.monthlyCost").value(mortgageReport.getMonthlyCost()));
    }

    @Test
    void testMortgageCheckPost_Wrong_maturityPeriod() throws Exception {

        MortgageData mortgageData = new MortgageData(new BigDecimal(50000), 50,
                new BigDecimal(150000), new BigDecimal(300000));

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mortgageData)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No rate found for maturity period: 50"));
    }

    private static Stream<Arguments> mortgageEvaluationCases() {
        return Stream.of(
                givenMortgageInputs("35000", 30, "100000", "250000", true, "632.07"),
                givenMortgageInputs("36000", 10, "150000", "450000", false, "1483.29"),
                givenMortgageInputs("60000", 15, "100000", "200000", true, "739.69"),
                givenMortgageInputs("30000", 15, "100000", "200000", false, "739.69"),
                givenMortgageInputs("70000", 20, "400000", "300000", false, "2530.60"),
                givenMortgageInputs("20000", 10, "200000", "150000", false, "1977.72"),
                givenMortgageInputs("30339.36", 30, "100000", "150000", true, "632.07"),
                givenMortgageInputs("30339.35", 30, "100000", "150000", false, "632.07"),
                givenMortgageInputs("90000", 10, "100000", "100000", true, "988.86"),
                givenMortgageInputs("80000", 10, "50000", "100000", true, "494.43"),
                givenMortgageInputs("40000", 25, "50000", "500000", true, "292.30")
        );
    }

    private static Arguments givenMortgageInputs(String income, int maturityPeriod, String loanValue,
                                                 String homeValue, boolean feasible, String expectedMonthly) {
        return Arguments.of(
                new MortgageData(new BigDecimal(income), maturityPeriod, new BigDecimal(loanValue), new BigDecimal(homeValue)),
                new MortgageReport(feasible, new BigDecimal(expectedMonthly))
        );
    }
}

