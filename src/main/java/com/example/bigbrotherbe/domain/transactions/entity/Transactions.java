package com.example.bigbrotherbe.domain.transactions.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactions_id")
    private Long id;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "type")
    private String type;

    @Column(name = "amount")
    private long amount;

    @Column(name = "balance")
    private long balance;

    @Column(name = "description")
    private String description;

    @Column(name = "affiliation_id")
    private Long affiliationId;

    // 거래일자, 구분, 거래금액, 거래 후 잔액, 거래내용 , 계좌번호
    // date, type, amount, balance, Description , accountNumber
}
