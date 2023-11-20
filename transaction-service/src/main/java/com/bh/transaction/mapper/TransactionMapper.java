package com.bh.transaction.mapper;

import com.bh.transaction.data.entity.TransactionEntity;
import com.bh.transaction.dto.request.TransactionRequest;
import com.bh.transaction.dto.response.TransactionResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionEntity convertRequestToEntity(TransactionRequest transactionRequest);

    TransactionResponse convertEntityToResponse(TransactionEntity transactionEntity);

    List<TransactionResponse> convertEntitiesToResponses(List<TransactionEntity> transactionEntities);
}
