package com.bh.account.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    CUSTOMER_NOT_FOUND_EXCEPTION(404, "customer with this id does not exist", HttpStatus.NOT_FOUND),
    TRANSACTION_SERVER_TIME_OUT(504,"Transaction server is not available please check it....",HttpStatus.GATEWAY_TIMEOUT);
    private final Integer code;
    private final String description;
    private final HttpStatus httpStatus;

    ErrorCode(Integer code, String description, HttpStatus httpStatus) {
        this.code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }
}
