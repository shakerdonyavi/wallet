package com.bh.transaction.service;

import com.bh.transaction.data.entity.TransactionEntity;
import com.bh.transaction.data.repository.TransactionRepository;
import com.bh.transaction.dto.request.TransactionRequest;
import com.bh.transaction.dto.response.TransactionResponse;
import com.bh.transaction.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public void makeTransaction(TransactionRequest transactionRequest) {
        TransactionEntity transactionEntity = transactionMapper.convertRequestToEntity(transactionRequest);
        log.info("transaction request with account id:{} and initial balance:{} receive",
                transactionRequest.accountId(), transactionRequest.amount());
        transactionRepository.save(transactionEntity);
    }

    public List<TransactionResponse> getTransactions(Long accountId) {
        List<TransactionEntity> transactionEntities = transactionRepository.findByAccountId(accountId);
        return transactionMapper.convertEntitiesToResponses(transactionEntities);
    }
}
