package com.bh.transaction.controller;

import com.bh.transaction.dto.request.TransactionRequest;
import com.bh.transaction.dto.response.TransactionResponse;
import com.bh.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/make-transaction")
    public void create(@RequestBody TransactionRequest transactionRequest) {
        transactionService.makeTransaction(transactionRequest);
    }

    @GetMapping("/{account-id}/transactions")
    public List<TransactionResponse> getTransactions(@PathVariable(name = "account-id") Long accountId) {
        return transactionService.getTransactions(accountId);
    }
}
