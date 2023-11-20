package com.bh.account.controller;

import com.bh.account.dto.request.AccountRequest;
import com.bh.account.dto.response.AccountTransactionResponse;
import com.bh.account.service.AccountService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/open-account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "404", description = "Bad Request",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "    \"code\": 404,\n" +
                                    "    \"message\": \"customer with this id does not exist\"\n" +
                                    "}"))),
            @ApiResponse(responseCode = "504", description = "Gateway Timeout",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "    \"code\": 504,\n" +
                                    "    \"message\": \"Transaction server is not available please check it....\"\n" +
                                    "}")))
    })
    void openAccount(@RequestBody AccountRequest accountRequest) {
        accountService.openAccount(accountRequest);
    }

    @GetMapping("/account-transactions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful"),
            @ApiResponse(responseCode = "504", description = "Gateway Timeout",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\n" +
                                    "    \"code\": 504,\n" +
                                    "    \"message\": \"Transaction server is not available please check it....\"\n" +
                                    "}")))
    })
    List<AccountTransactionResponse> getAccountTransactions() {
        return accountService.getAccountTransactions();
    }
}
