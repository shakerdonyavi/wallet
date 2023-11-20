package com.bh.account.transactionservice;

import com.bh.account.dto.request.TransactionRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "transaction-service", path = "/api/v1/transaction")
public interface TransactionServiceClient {

    @PostMapping("/make-transaction")
    void makeTransaction(@RequestBody TransactionRequest transactionRequest);

    @GetMapping("/{account-id}/transactions")
    List<TransactionResponse> getTransactions(@PathVariable(name = "account-id") Long accountId);
}
