package com.finflow.finflow.controller;

import com.finflow.finflow.repository.TransactionRepository;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final TransactionRepository repository;
    public DashboardController(TransactionRepository repository) { this.repository = repository; }

    @GetMapping("/summary")
    public Map<String, Object> summary() {
        LocalDate now = LocalDate.now();
        LocalDate start = now.withDayOfMonth(1);
        BigDecimal income = repository.sumByTypeAndDate("INCOME", start, now);
        BigDecimal expenses = repository.sumByTypeAndDate("EXPENSE", start, now);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("month", now.getMonth().toString());
        result.put("income", income);
        result.put("expenses", expenses);
        result.put("balance", income.subtract(expenses));
        result.put("transactionCount", repository.count());
        result.put("recentTransactions", repository.findAllByOrderByTransactionDateDescIdDesc().stream().limit(5).toList());
        return result;
    }
}
