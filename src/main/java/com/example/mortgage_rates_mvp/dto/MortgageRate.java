package com.example.mortgage_rates_mvp.dto;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MortgageRate {
    private int maturityPeriod;

    @NonNull
    private BigDecimal interestRate;

    @NonNull
    private Timestamp lastUpdate;
}
