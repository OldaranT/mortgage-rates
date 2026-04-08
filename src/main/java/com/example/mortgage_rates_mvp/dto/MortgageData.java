package com.example.mortgage_rates_mvp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Input data required to evaluate mortgage eligibility")
public class MortgageData {

    @NonNull
    @Schema(
            description = "Gross yearly income of the applicant in euros (before taxes)",
            example = "60000.00"
    )
    private BigDecimal income;

    @NonNull
    @Schema(
            description = "Mortgage duration in years",
            example = "30"
    )
    private Integer maturityPeriod;

    @NonNull
    @Schema(
            description = "Requested loan amount in euros",
            example = "250000.00"
    )
    private BigDecimal loanValue;

    @NonNull
    @Schema(
            description = "Estimated market value of the property in euros",
            example = "300000.00"
    )
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
