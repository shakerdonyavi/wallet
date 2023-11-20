package com.bh.account.dto.response;

import com.bh.account.transactionservice.TransactionResponse;

import java.util.List;

public record AccountTransactionResponse(String name, String surname, Long balance,
                                         List<TransactionResponse> transactions) {
}
