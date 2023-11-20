package com.bh.account.service;

import com.bh.account.data.entity.CustomerEntity;
import com.bh.account.data.repository.CustomerRepository;
import com.bh.account.dto.request.CustomerRequest;
import com.bh.account.dto.response.CustomerResponse;
import com.bh.account.exception.BusinessException;
import com.bh.account.exception.ErrorCode;
import com.bh.account.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        validateRequest(customerRequest);
        CustomerEntity customerEntity = customerMapper.convertRequestToEntity(customerRequest);
        customerEntity = customerRepository.save(customerEntity);
        return customerMapper.convertEntityToResponse(customerEntity);
    }

    private void validateRequest(CustomerRequest customerRequest) {
        if (customerRequest.name() == null || customerRequest.name().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.CUSTOMER_NAME_IS_EMPTY);
        }
        if (customerRequest.surname() == null || customerRequest.surname().trim().isEmpty()) {
            throw new BusinessException(ErrorCode.CUSTOMER_SURNAME_IS_EMPTY);
        }
    }

    public List<CustomerResponse> getCustomers() {
        return customerMapper.convertEntitiesToResponses(customerRepository.findAll());
    }

    public CustomerResponse getCustomerInfo(Long customerId) {
        Optional<CustomerEntity> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            return customerMapper.convertEntityToResponse(customer.get());
        } else {
            log.warn("customer with id:{} does not exist in data base", customer);
            throw new BusinessException(ErrorCode.CUSTOMER_NOT_FOUND_EXCEPTION);
        }
    }
}
