package com.example.mortgage_rates_mvp.service;

import com.example.mortgage_rates_mvp.dto.MortgageData;
import com.example.mortgage_rates_mvp.dto.MortgageRate;
import com.example.mortgage_rates_mvp.dto.MortgageReport;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Service
public class MortgageService {

    private static final BigDecimal MAX_PAYMENT_RATIO = BigDecimal.valueOf(0.25);
    private static final int MONTHS_IN_YEAR = 12;
    private static final Timestamp CONSTANT_TIME_STAMP = Timestamp.valueOf(LocalDateTime
            .of(2025, 5, 21, 0, 0, 0));

    private final List<MortgageRate> mortgageRates = List.of(
            new MortgageRate(10, new BigDecimal("3.5"), CONSTANT_TIME_STAMP),
            new MortgageRate(15, new BigDecimal("4.0"), CONSTANT_TIME_STAMP),
            new MortgageRate(20, new BigDecimal("4.5"), CONSTANT_TIME_STAMP),
            new MortgageRate(25, new BigDecimal("5.0"), CONSTANT_TIME_STAMP),
            new MortgageRate(30, new BigDecimal("6.5"), CONSTANT_TIME_STAMP)
    );

    public MortgageReport evaluateMortgage(MortgageData mortgageData) {

        int maturityPeriod = mortgageData.getMaturityPeriod();
        BigDecimal loanValue = mortgageData.getLoanValue();

        MortgageRate mortgageRate = getRateByMaturity(maturityPeriod);

        BigDecimal monthlyPayment = calculateMonthlyPayment(
                loanValue,
                mortgageRate.getInterestRate(),
                maturityPeriod);

        // - a mortgage should not exceed 4 times the income
        // - a mortgage should not exceed the home value
        boolean feasible = incomeCheck(monthlyPayment, mortgageData.getIncome()) &&
                allowedHomeValue(loanValue, mortgageData.getHomeValue());

        return new MortgageReport(feasible, monthlyPayment);
    }

    private BigDecimal calculateMonthlyPayment(BigDecimal principal, BigDecimal rate, int maturityPeriod) {

        /*
        * Formula from https://en.wikipedia.org/wiki/Mortgage_calculator
        * Monthly Payment (M) = P × [ r × (1 + r)^n ] / [ (1 + r)^n − 1 ]
        * P = principal(Loan)
        * r(monthlyRate) = rate / 12 * 100(100 because of percentage)
        * n(totalMonthlyPayments) = maturityPeriod * 12(12 months in a year)
        *
        * plusRatePow = (1 + r)^n
        * numerator = r × plusRatePow
        * denominator = plusRatePow− 1
        *
        * M = P x numerator/denominator
         */

        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(MONTHS_IN_YEAR * 100), 10, RoundingMode.HALF_UP);
        int totalMonthlyPayments = maturityPeriod * MONTHS_IN_YEAR;

        // plusRatePow = (1 + r)^n
        BigDecimal plusRatePow = BigDecimal.ONE.add(monthlyRate).pow(totalMonthlyPayments);
        // numerator = r × plusRatePow
        BigDecimal numerator = monthlyRate.multiply(plusRatePow);
        // denominator = plusRatePow− 1
        BigDecimal denominator = plusRatePow.subtract(BigDecimal.ONE);

        // Monthly Payment = Principal x numerator/denominator
        return principal.multiply(numerator).divide(denominator, 2, RoundingMode.HALF_UP);
    }

    private boolean incomeCheck(BigDecimal monthlyPayment, BigDecimal annualIncome) {
        BigDecimal monthlyIncome = annualIncome.divide(BigDecimal.valueOf(MONTHS_IN_YEAR), 10, RoundingMode.HALF_UP);
        BigDecimal maxPayment = monthlyIncome.multiply(MAX_PAYMENT_RATIO);

        return monthlyPayment.compareTo(maxPayment) <= 0;
    }

    private boolean allowedHomeValue(BigDecimal loanValue, BigDecimal homeValue) {
        return loanValue.compareTo(homeValue) <= 0;
    }

    private MortgageRate getRateByMaturity(int maturityPeriod) {
        return this.mortgageRates.stream()
                .filter(x -> x.getMaturityPeriod() == maturityPeriod)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No rate found for maturity period: " + maturityPeriod));
    }
}
