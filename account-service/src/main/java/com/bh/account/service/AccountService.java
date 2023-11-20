package com.bh.account.service;

import com.bh.account.data.entity.AccountEntity;
import com.bh.account.data.repository.AccountRepository;
import com.bh.account.dto.request.AccountRequest;
import com.bh.account.dto.request.TransactionRequest;
import com.bh.account.dto.response.AccountTransactionResponse;
import com.bh.account.exception.BusinessException;
import com.bh.account.exception.ErrorCode;
import com.bh.account.mapper.AccountMapper;
import com.bh.account.transactionservice.TransactionResponse;
import com.bh.account.transactionservice.TransactionServiceClient;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CustomerService customerService;
    private final TransactionServiceClient transactionServiceClient;

    @Transactional
    public void openAccount(AccountRequest accountRequest) {
        customerService.getCustomerInfo(accountRequest.customerId());
        AccountEntity accountEntity = accountMapper.convertRequestToEntity(accountRequest);
        accountEntity = accountRepository.save(accountEntity);
        if (! Objects.equals(0L, accountRequest.initialBalance())) {
            log.info("the value of initial balance for open account for account id :{} is :{}",
                    accountEntity.getInitialBalance(), accountRequest.customerId());
            makeTransaction(accountRequest, accountEntity);
            accountEntity.setBalance(0L);
            accountRepository.save(accountEntity);
        }
    }

    public void makeTransaction(AccountRequest accountRequest, AccountEntity accountEntity) {
        try {
            TransactionRequest transactionRequest = new TransactionRequest(accountEntity.getId(),
                    accountRequest.initialBalance());
            transactionServiceClient.makeTransaction(transactionRequest);
        } catch (FeignException.ServiceUnavailable exception) {
            log.error("transaction server is not available");
            throw new BusinessException(ErrorCode.TRANSACTION_SERVER_TIME_OUT);
        }
    }

    public List<AccountTransactionResponse> getAccountTransactions() {
        List<AccountTransactionResponse> customerTransactions = new ArrayList<>();
        List<AccountEntity> accounts = accountRepository.findAll();
        accounts.forEach(account -> {
            List<TransactionResponse> transactions = getTransactionByAccountId(account.getId());
            AccountTransactionResponse accountTransactionResponse =
                    new AccountTransactionResponse(account.getCustomer().getName(),
                            account.getCustomer().getSurname(), account.getInitialBalance(), transactions);
            customerTransactions.add(accountTransactionResponse);
        });
        return customerTransactions;
    }

    private List<TransactionResponse> getTransactionByAccountId(Long accountId) {
        try {
            return transactionServiceClient.getTransactions(accountId);
        } catch (FeignException.ServiceUnavailable exception) {
            log.error("transaction server is not available");
            throw new BusinessException(ErrorCode.TRANSACTION_SERVER_TIME_OUT);
        }
    }
}
