package com.finflow.finflow.repository;

import com.finflow.finflow.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByOrderByTransactionDateDescIdDesc();
    List<Transaction> findByTransactionDateBetweenOrderByTransactionDateDesc(LocalDate start, LocalDate end);
    @Query("select coalesce(sum(t.amount), 0) from Transaction t where t.type = :type and t.transactionDate between :start and :end")
    BigDecimal sumByTypeAndDate(@Param("type") String type, @Param("start") LocalDate start, @Param("end") LocalDate end);
}
