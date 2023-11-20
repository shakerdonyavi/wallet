package com.bh.account.mapper;

import com.bh.account.data.entity.CustomerEntity;
import com.bh.account.dto.request.CustomerRequest;
import com.bh.account.dto.response.CustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerEntity convertRequestToEntity(CustomerRequest customerRequest);

    @Mapping(source = "id", target = "customerId")
    CustomerResponse convertEntityToResponse(CustomerEntity customerEntity);

    List<CustomerResponse> convertEntitiesToResponses(List<CustomerEntity> customerEntities);
}
