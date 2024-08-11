package com.example.bigbrotherbe.domain.transactions.repository;

import com.example.bigbrotherbe.domain.transactions.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionsRepository extends JpaRepository<Transactions, Long> {
}
