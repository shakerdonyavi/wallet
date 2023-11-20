package com.bh.ui.controller;


import com.bh.ui.dto.request.AccountRequest;
import com.bh.ui.dto.request.CustomerRequest;
import com.bh.ui.dto.response.AccountTransactionResponse;
import com.bh.ui.dto.response.CustomerResponse;
import com.bh.ui.service.AccountServiceClient;
import com.bh.ui.service.CustomerServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final AccountServiceClient accountServiceClient;
    private final CustomerServiceClient customerServiceClient;

    @GetMapping("/home")
    public String grid(Model model) {
        List<AccountTransactionResponse> accountTransactions = accountServiceClient.getAccountTransactions();
        model.addAttribute("data", accountTransactions);
        List<CustomerResponse> customers = customerServiceClient.getCustomers();
        model.addAttribute("customers", customers);
        return "home";
    }

    @PostMapping("/createCustomer")
    public String createCustomer(@RequestParam String name, @RequestParam String surname, Model model) {
        CustomerRequest customerRequest = new CustomerRequest(name, surname);
        customerServiceClient.createCustomer(customerRequest);
        List<AccountTransactionResponse> accountTransactions = accountServiceClient.getAccountTransactions();
        model.addAttribute("data", accountTransactions);
        return "redirect:/home";
    }

    @PostMapping("/openAccount")
    public String openAccount(@RequestParam Long customerId, @RequestParam(required = false) Long initialBalance,
                              Model model) {
        initialBalance = Objects.isNull(initialBalance) ? 0 : initialBalance;
        AccountRequest accountRequest = new AccountRequest(customerId, initialBalance);
        accountServiceClient.openAccount(accountRequest);
        List<AccountTransactionResponse> accountTransactions = accountServiceClient.getAccountTransactions();
        model.addAttribute("data", accountTransactions);
        return "redirect:/home";
    }
}
