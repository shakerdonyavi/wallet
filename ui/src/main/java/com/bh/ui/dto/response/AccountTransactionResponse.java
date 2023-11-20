package com.bh.ui.dto.response;

import java.util.List;

public record AccountTransactionResponse(String name, String surname, Long balance,
                                         List<TransactionResponse> transactions) {
}

