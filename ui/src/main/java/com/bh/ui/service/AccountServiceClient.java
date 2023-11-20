package com.bh.ui.service;

import com.bh.ui.dto.request.AccountRequest;
import com.bh.ui.dto.response.AccountTransactionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient(name = "account-service", url = "${account.server.url}")
public interface AccountServiceClient {
    @GetMapping("/api/v1/account/account-transactions")
    List<AccountTransactionResponse> getAccountTransactions();

    @PostMapping("/api/v1/account/open-account")
    void openAccount(AccountRequest accountRequest);
}
