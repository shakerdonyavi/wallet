package com.bh.account.service;

import com.bh.account.data.entity.AccountEntity;
import com.bh.account.data.entity.CustomerEntity;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {AccountService.class})
@ExtendWith(SpringExtension.class)
class AccountServiceTest {
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private AccountMapper accountMapper;
    @Autowired
    private AccountService accountService;
    @MockBean
    private CustomerService customerService;
    @MockBean
    private TransactionServiceClient transactionServiceClient;

    @Test
    void openAccount_withInitialBalance_shouldMakeTransactionAndSaveEntity() {
        // Arrange
        long customerId = 1L;
        long initialBalance = 100L;
        AccountRequest accountRequest = new AccountRequest(customerId, initialBalance);
        CustomerEntity customerEntity = createCustomer();
        AccountEntity accountEntity = createAccount(customerEntity);
        when(accountMapper.convertRequestToEntity(accountRequest)).thenReturn(accountEntity);
        when(accountRepository.save(accountEntity)).thenReturn(accountEntity);
        TransactionRequest transactionRequest = new TransactionRequest(accountEntity.getId(), initialBalance);
        // Act
        accountService.openAccount(accountRequest);
        // Assert
        Mockito.verify(customerService).getCustomerInfo(customerId);
        Mockito.verify(accountMapper).convertRequestToEntity(accountRequest);
        Mockito.verify(accountRepository, Mockito.times(2)).save(accountEntity);
        Mockito.verify(transactionServiceClient).makeTransaction(transactionRequest);
    }

    @Test
    void openAccount_withoutInitialBalance_shouldNotMakeTransactionAndSaveEntity() {
        // Arrange
        long customerId = 1L;
        long initialBalance = 100L;
        AccountRequest accountRequest = new AccountRequest(customerId, initialBalance);
        CustomerEntity customerEntity = createCustomer();
        AccountEntity accountEntity = createAccount(customerEntity);
        when(accountMapper.convertRequestToEntity(accountRequest)).thenReturn(accountEntity);
        when(accountRepository.save(accountEntity)).thenReturn(accountEntity);
        TransactionRequest transactionRequest = new TransactionRequest(accountEntity.getId(), initialBalance);
        Mockito.doThrow(FeignException.ServiceUnavailable.class)
                .when(transactionServiceClient).makeTransaction(transactionRequest);
        // Act and Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> accountService.openAccount(accountRequest));
        assertEquals(ErrorCode.TRANSACTION_SERVER_TIME_OUT, exception.getErrorCode());
    }

    @Test
    void testGetAccountTransactions_whenFeignExceptionServiceUnavailable() {
        // Arrange
        CustomerEntity customer = createCustomer();
        AccountEntity account = createAccount(customer);
        List<AccountEntity> mockAccounts = List.of(account);
        when(accountRepository.findAll()).thenReturn(mockAccounts);
        when(transactionServiceClient.getTransactions(Mockito.anyLong()))
                .thenThrow(FeignException.ServiceUnavailable.class);
        // Act and Assert
        BusinessException exception = assertThrows(BusinessException.class,
                () -> accountService.getAccountTransactions());

        assertEquals(ErrorCode.TRANSACTION_SERVER_TIME_OUT, exception.getErrorCode());
    }

    @Test
    void testGetAccountTransactions() {
        // Arrange
        CustomerEntity customer = createCustomer();
        AccountEntity account = createAccount(customer);
        List<AccountEntity> mockAccounts = List.of(account);
        when(accountRepository.findAll()).thenReturn(mockAccounts);
        List<TransactionResponse> mockTransactions = List.of(createTransactionResponse());
        when(transactionServiceClient.getTransactions(Mockito.anyLong())).thenReturn(mockTransactions);
        // Act
        List<AccountTransactionResponse> result = accountService.getAccountTransactions();
        // Assert
        assertEquals(mockAccounts.size(), result.size());
    }

    private CustomerEntity createCustomer() {
        return CustomerEntity.builder()
                .id(1L)
                .name("mona")
                .surname("shaker")
                .build();
    }

    private AccountEntity createAccount(CustomerEntity customer) {
        return AccountEntity.builder()
                .id(1L)
                .customer(customer)
                .initialBalance(100L)
                .build();
    }

    private TransactionResponse createTransactionResponse() {
        return new TransactionResponse(new Date(), 1000L);
    }

}