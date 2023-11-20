package com.bh.ui.service;

import com.bh.ui.dto.request.CustomerRequest;
import com.bh.ui.dto.response.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "customer-service", url = "${account.server.url}")
public interface CustomerServiceClient {
    @PostMapping("/api/v1/customer/create")
    CustomerResponse createCustomer(@RequestBody CustomerRequest customerRequest);

    @GetMapping("/api/v1/customer")
    List<CustomerResponse> getCustomers();
}
