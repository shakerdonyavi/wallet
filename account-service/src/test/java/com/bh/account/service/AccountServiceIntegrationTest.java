package com.bh.account.service;

import com.bh.account.data.entity.AccountEntity;
import com.bh.account.data.repository.AccountRepository;
import com.bh.account.dto.request.AccountRequest;
import com.bh.account.dto.request.CustomerRequest;
import com.bh.account.dto.response.CustomerResponse;
import com.bh.account.transactionservice.TransactionServiceClient;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class AccountServiceIntegrationTest {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @MockBean
    private TransactionServiceClient transactionServiceClient;

    @Container
    static PostgreSQLContainer<?> dbContainer =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("postgres")
                    .withUsername("sa")
                    .withPassword("sa");


    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> dbContainer.getJdbcUrl());
        registry.add("spring.datasource.username", dbContainer::getPassword);
        registry.add("spring.datasource.password", dbContainer::getUsername);
        registry.add("eureka.client.enabled", () -> false);
    }

    @TestConfiguration
    public static class TestConfig {
        @Bean
        public EurekaClientConfigBean eurekaClientConfigBean() {
            EurekaClientConfigBean config = new EurekaClientConfigBean();
            config.setRegistryFetchIntervalSeconds(1);
            config.setInstanceInfoReplicationIntervalSeconds(2);
            config.setInitialInstanceInfoReplicationIntervalSeconds(3);
            return config;
        }
    }

    @Test
    void testOpenAccount() {
        CustomerRequest customerRequest = new CustomerRequest("mona", "shaker");
        CustomerResponse customer = customerService.createCustomer(customerRequest);
        AccountRequest accountRequest = new AccountRequest(customer.customerId(), 1000L);
        Mockito.doNothing().when(transactionServiceClient).makeTransaction(any());
        accountService.openAccount(accountRequest);
        List<AccountEntity> accounts = accountRepository.findAll();
        assertEquals(1, accounts.size());
        AccountEntity accountEntity = accounts.get(0);
        assertEquals(0, accountEntity.getBalance());
        assertEquals(1000L, accountEntity.getInitialBalance());
        assertEquals(customer.customerId(), accountEntity.getCustomer().getId());
    }
}
