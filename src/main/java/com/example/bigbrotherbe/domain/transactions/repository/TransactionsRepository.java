package com.example.bigbrotherbe.domain.transactions.repository;

import com.example.bigbrotherbe.domain.transactions.dto.response.TransactionsResponse;
import com.example.bigbrotherbe.domain.transactions.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionsRepository extends JpaRepository<Transactions, Long> {

    @Query("SELECT t FROM Transactions t " +
            "WHERE FUNCTION('DATE_FORMAT', t.date, '%Y-%m') = :yearMonth " +
            "AND t.affiliationId = :affiliationId " +
            "ORDER BY t.date ASC")
    List<Transactions> findAllByYearMonthAndAffiliationId(@Param("yearMonth") String yearMonth,
                                                          @Param("affiliationId") Long affiliationId);
}
