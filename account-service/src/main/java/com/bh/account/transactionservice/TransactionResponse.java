package com.bh.account.transactionservice;

import java.util.Date;

public record TransactionResponse(Date transactionDate, Long amount) {
}
