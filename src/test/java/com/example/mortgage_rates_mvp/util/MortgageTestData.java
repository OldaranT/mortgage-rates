package com.example.mortgage_rates_mvp.util;

import com.example.mortgage_rates_mvp.dto.MortgageData;
import com.example.mortgage_rates_mvp.dto.MortgageReport;
import org.junit.jupiter.params.provider.Arguments;

import java.math.BigDecimal;
import java.util.stream.Stream;

public class MortgageTestData {

    private static Stream<Arguments> mortgageEvaluationCases() {
        return Stream.of(
                calculationCheck(),
                feasibleStandardCase(),
                feasibleAtMinimumIncome(),
                feasibleExactValueMatch(),
                feasibleLongTermLowLoan(),
                incomeTooLowForShortTerm(),
                incomeTooLowForMediumTerm(),
                bothIncomeAndValueInvalid(),
                loanExceedsHomeValue(),
                smallLoanHighIncome(),
                roundingCaseJustFeasible(),
                roundingCaseJustNotFeasible()
        );
    }

    private static Arguments givenMortgageInputs(String income, int years, String loan, String home, boolean feasible, String expectedMonthly) {
        return Arguments.of(
                new MortgageData(new BigDecimal(income), years, new BigDecimal(loan), new BigDecimal(home)),
                new MortgageReport(feasible, new BigDecimal(expectedMonthly))
        );
    }

    private static Arguments calculationCheck() {
        // The values are based of calculation source to check if the calculation is still correct. Source: https://en.wikipedia.org/wiki/Mortgage_calculator
        return givenMortgageInputs("90000", 30, "200000", "250000", true, "1264.14");
    }

    private static Arguments feasibleStandardCase() {
        return givenMortgageInputs("60000", 15, "100000", "200000", true, "739.69");
    }

    private static Arguments feasibleAtMinimumIncome() {
        return givenMortgageInputs("35000", 30, "100000", "250000", true, "632.07");
    }

    private static Arguments feasibleExactValueMatch() {
        return givenMortgageInputs("47465.28", 10, "100000", "100000", true, "988.86");
    }

    private static Arguments feasibleLongTermLowLoan() {
        return givenMortgageInputs("40000", 25, "50000", "500000", true, "292.30");
    }

    private static Arguments incomeTooLowForShortTerm() {
        return givenMortgageInputs("36000", 10, "150000", "450000", false, "1483.29");
    }

    private static Arguments incomeTooLowForMediumTerm() {
        return givenMortgageInputs("30000", 15, "100000", "200000", false, "739.69");
    }

    private static Arguments bothIncomeAndValueInvalid() {
        return givenMortgageInputs("20000", 10, "200000", "150000", false, "1977.72");
    }

    private static Arguments loanExceedsHomeValue() {
        return givenMortgageInputs("70000", 20, "200000", "100000", false, "1265.30");
    }

    private static Arguments smallLoanHighIncome() {
        return givenMortgageInputs("80000", 10, "50000", "100000", true, "494.43");
    }

    private static Arguments roundingCaseJustFeasible() {
        return givenMortgageInputs("30339.36", 30, "100000", "150000", true, "632.07");
    }

    private static Arguments roundingCaseJustNotFeasible() {
        return givenMortgageInputs("30339.35", 30, "100000", "150000", false, "632.07");
    }
}
