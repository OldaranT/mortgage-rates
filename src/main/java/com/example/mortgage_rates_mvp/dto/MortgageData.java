package com.example.mortgage_rates_mvp.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MortgageData {

    @NonNull
    private BigDecimal income;

    @NonNull
    private Integer maturityPeriod;

    @NonNull
    private BigDecimal loanValue;

    @NonNull
    private BigDecimal homeValue;

    @Override
    public String toString(){
        return """
        MortgageData {
            income: %s
            maturityPeriod: %s
            loanValue: %s
            homeValue: %s
        }
        """.formatted(income, maturityPeriod, loanValue, homeValue);
    }
}
