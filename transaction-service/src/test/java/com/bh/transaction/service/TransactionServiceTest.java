package com.bh.transaction.service;

import com.bh.transaction.data.entity.TransactionEntity;
import com.bh.transaction.data.repository.TransactionRepository;
import com.bh.transaction.dto.request.TransactionRequest;
import com.bh.transaction.dto.response.TransactionResponse;
import com.bh.transaction.mapper.TransactionMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {TransactionService.class})
@ExtendWith(SpringExtension.class)
class TransactionServiceTest {
    @MockBean
    private TransactionRepository transactionRepository;
    @MockBean
    private TransactionMapper transactionMapper;
    @Autowired
    private TransactionService transactionService;

    @Test
    void testSaveTransaction() {
        // Arrange
        TransactionRequest mockTransactionRequest = new TransactionRequest(1L, 100L);
        TransactionEntity mockTransactionEntity = createTransaction();
        when(transactionMapper.convertRequestToEntity(mockTransactionRequest)).thenReturn(mockTransactionEntity);
        // Act
        transactionService.makeTransaction(mockTransactionRequest);
        // Assert
        verify(transactionMapper).convertRequestToEntity(mockTransactionRequest);
        verify(transactionRepository).save(mockTransactionEntity);
    }

    @Test
    void testGetTransactions() {
        // Arrange
        Long accountId = 1L;
        List<TransactionEntity> mockTransactionEntities = List.of(createTransaction());
        when(transactionRepository.findByAccountId(accountId)).thenReturn(mockTransactionEntities);

        List<TransactionResponse> mockTransactionResponses = List.of(createTransactionResponse());
        when(transactionMapper.convertEntitiesToResponses(mockTransactionEntities)).thenReturn(mockTransactionResponses);
        // Act
        List<TransactionResponse> result = transactionService.getTransactions(accountId);
        // Assert
        assertEquals(mockTransactionResponses, result);
    }

    private TransactionEntity createTransaction() {
        return TransactionEntity.builder()
                .transactionDate(new Date())
                .accountId(1L)
                .amount(1000L)
                .build();
    }

    private TransactionResponse createTransactionResponse() {
        return new TransactionResponse(new Date(), 1000L);
    }

}