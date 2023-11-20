package com.bh.account.service;

import com.bh.account.data.entity.CustomerEntity;
import com.bh.account.data.repository.CustomerRepository;
import com.bh.account.dto.request.CustomerRequest;
import com.bh.account.dto.response.CustomerResponse;
import com.bh.account.exception.BusinessException;
import com.bh.account.exception.ErrorCode;
import com.bh.account.mapper.CustomerMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {CustomerService.class})
@ExtendWith(SpringExtension.class)
class CustomerServiceTest {
    @MockBean
    private CustomerRepository customerRepository;
    @MockBean
    private CustomerMapper customerMapper;
    @Autowired
    private CustomerService customerService;

    @Test
    void testCreateCustomer() {
        // Arrange
        CustomerRequest mockCustomerRequest = new CustomerRequest("mona", "shaker");
        CustomerEntity mockCustomerEntity = createCustomer();
        CustomerResponse mockCustomerResponse = createCustomerResponse();
        when(customerMapper.convertRequestToEntity(mockCustomerRequest)).thenReturn(mockCustomerEntity);
        when(customerRepository.save(mockCustomerEntity)).thenReturn(mockCustomerEntity);
        when(customerMapper.convertEntityToResponse(mockCustomerEntity)).thenReturn(mockCustomerResponse);
        // Act
        CustomerResponse result = customerService.createCustomer(mockCustomerRequest);
        // Assert
        assertEquals(mockCustomerResponse, result);
    }


    @Test
    void testGetCustomerInfo_whenCustomerExists() {
        // Arrange
        Long customerId = 1L;
        CustomerEntity mockCustomerEntity = createCustomer();
        CustomerResponse mockCustomerResponse = createCustomerResponse();
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(mockCustomerEntity));
        when(customerMapper.convertEntityToResponse(mockCustomerEntity)).thenReturn(mockCustomerResponse);
        // Act
        CustomerResponse result = customerService.getCustomerInfo(customerId);
        // Assert
        assertEquals(mockCustomerResponse, result);
    }

    @Test
    void testGetCustomerInfo_whenCustomerDoesNotExist() {
        // Arrange
        Long customerId = 2L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        // Act and Assert
        CustomerService yourServiceClass;
        BusinessException exception = assertThrows(BusinessException.class,
                () -> customerService.getCustomerInfo(customerId));
        assertEquals(ErrorCode.CUSTOMER_NOT_FOUND_EXCEPTION, exception.getErrorCode());
    }
    private CustomerEntity createCustomer() {
        return CustomerEntity.builder()
                .id(1L)
                .name("mona")
                .surname("shaker")
                .build();
    }

    private CustomerResponse createCustomerResponse() {
        return new CustomerResponse(1L, "mona", "shaker");
    }
}