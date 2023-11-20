package com.bh.account.exception;

import com.bh.account.dto.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@ControllerAdvice
public class AppExceptionsHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {BusinessException.class})
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception) {
        ErrorResponse errorResponse = getErrorResponse(exception);
        return new ResponseEntity<>(errorResponse, exception.getErrorCode().getHttpStatus());
    }

    private static ErrorResponse getErrorResponse(BusinessException exception) {
        return ErrorResponse.builder()
                .code(exception.getErrorCode().getCode())
                .message(exception.getErrorCode().getDescription())
                .build();
    }
}
