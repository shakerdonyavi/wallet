package com.bh.transaction.dto.response;

import java.util.Date;

public record TransactionResponse(Date transactionDate, Long amount) {
}
