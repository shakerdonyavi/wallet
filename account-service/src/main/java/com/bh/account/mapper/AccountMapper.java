package com.bh.account.mapper;

import com.bh.account.data.entity.AccountEntity;
import com.bh.account.dto.request.AccountRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(source = "customerId", target = "customer.id")
    @Mapping(source = "initialBalance", target = "balance")
    AccountEntity convertRequestToEntity(AccountRequest accountRequest);
}
