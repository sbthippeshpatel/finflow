package com.finflow.finflow.controller;

import com.finflow.finflow.model.Transaction;
import com.finflow.finflow.repository.TransactionRepository;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/assistant")
public class AssistantController {
    private final TransactionRepository repository;
    public AssistantController(TransactionRepository repository) { this.repository = repository; }

    @PostMapping("/chat")
    public Map<String, String> chat(@RequestBody Map<String, String> request) {
        String message = request.getOrDefault("message", "").toLowerCase();
        LocalDate today = LocalDate.now();
        BigDecimal income = repository.sumByTypeAndDate("INCOME", today.withDayOfMonth(1), today);
        BigDecimal expenses = repository.sumByTypeAndDate("EXPENSE", today.withDayOfMonth(1), today);
        String reply;
        if (message.contains("saving") || message.contains("save")) {
            BigDecimal balance = income.subtract(expenses);
            reply = "This month, your recorded income is ₹" + income + " and expenses are ₹" + expenses + ". Your net is ₹" + balance + ". Try setting a weekly spending limit and reviewing your largest expense categories.";
        } else if (message.contains("expense") || message.contains("spend")) {
            var top = repository.findAllByOrderByTransactionDateDescIdDesc().stream().filter(t -> "EXPENSE".equals(t.getType())).limit(5).toList();
            reply = top.isEmpty() ? "You have no expenses recorded yet. Add a transaction to start tracking your spending." : "Your latest expenses: " + top.stream().map(t -> t.getTitle() + " (₹" + t.getAmount() + ")").reduce((a,b) -> a + ", " + b).orElse("") + ". Review recurring purchases to find possible savings.";
        } else {
            reply = "Hi! I'm FinFlow Assistant. This month you've recorded ₹" + income + " income and ₹" + expenses + " expenses. Ask me about saving or spending, or add transactions to get a more useful summary. This assistant provides basic budgeting guidance, not professional financial advice.";
        }
        return Map.of("reply", reply);
    }
}
