package com.example.bigbrotherbe.domain.transactions.dto.response;


import com.example.bigbrotherbe.domain.transactions.entity.Transactions;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


import java.time.LocalDateTime;


@Getter
@Builder
@AllArgsConstructor
public class TransactionsResponse {
    private Long transactionId;
    private String accountNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;
    private String type;
    private long amount;
    private long balance;
    private String description;
    private String note;

    public static TransactionsResponse fromTransactionsResponse(Transactions transactions) {
        return TransactionsResponse.builder()
                .transactionId(transactions.getId())
                .accountNumber(transactions.getAccountNumber())
                .date(transactions.getDate())
                .type(transactions.getType())
                .amount(transactions.getAmount())
                .balance(transactions.getBalance())
                .description(transactions.getDescription())
                .note(transactions.getNote())
                .build();
    }
}
