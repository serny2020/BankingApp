package com.microservices.accounts.dto;
import lombok.Data;


/**
 * DTO class representing account details.
 * Lombok's @Data generates getters, setters, equals, hashCode, and toString.
 */
@Data
public class AccountsDto {

    private Long accountNumber;

    private String accountType;

    private String branchAddress;
}
