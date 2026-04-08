package com.example.mortgage_rates_mvp.controller;

import com.example.mortgage_rates_mvp.dto.MortgageData;
import com.example.mortgage_rates_mvp.dto.MortgageRate;
import com.example.mortgage_rates_mvp.dto.MortgageReport;
import com.example.mortgage_rates_mvp.service.MortgageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MortgageController {
    private final MortgageService mortgageService;

    @GetMapping("/interest-rates")
    public ResponseEntity<List<MortgageRate>> interestRates() {
        return ResponseEntity.ok(mortgageService.getMortgageRates());
    }

    @PostMapping("/mortgage-check")
    public ResponseEntity<MortgageReport> mortgageCheck(@RequestBody MortgageData mortgageData) {
        return ResponseEntity.ok(mortgageService.evaluateMortgage(mortgageData));
    }
}
