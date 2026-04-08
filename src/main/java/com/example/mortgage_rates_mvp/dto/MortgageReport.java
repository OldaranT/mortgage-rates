package com.example.mortgage_rates_mvp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MortgageReport {
    private boolean feasible;

    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private BigDecimal monthlyCost;

    @Override
    public String toString(){
        return """
        MortgageReport {
            feasible: %s
            monthlyCost: %s
        }
        """.formatted(feasible, monthlyCost);
    }
}
