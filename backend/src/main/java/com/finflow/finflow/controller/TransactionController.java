package com.finflow.finflow.controller;

import com.finflow.finflow.model.Transaction;
import com.finflow.finflow.repository.TransactionRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionRepository repository;
    public TransactionController(TransactionRepository repository) { this.repository = repository; }

    @GetMapping
    public List<Transaction> all(@RequestParam(required = false) String search,
                                 @RequestParam(required = false) String type) {
        return repository.findAllByOrderByTransactionDateDescIdDesc().stream()
            .filter(t -> search == null || search.isBlank() || t.getTitle().toLowerCase().contains(search.toLowerCase()) || t.getCategory().toLowerCase().contains(search.toLowerCase()))
            .filter(t -> type == null || type.isBlank() || t.getType().equalsIgnoreCase(type))
            .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction create(@Valid @RequestBody Transaction transaction) {
        if (!"INCOME".equalsIgnoreCase(transaction.getType()) && !"EXPENSE".equalsIgnoreCase(transaction.getType())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "type must be INCOME or EXPENSE");
        }
        transaction.setType(transaction.getType().toUpperCase());
        if (transaction.getTransactionDate() == null) transaction.setTransactionDate(LocalDate.now());
        return repository.save(transaction);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!repository.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found");
        repository.deleteById(id);
    }
}
