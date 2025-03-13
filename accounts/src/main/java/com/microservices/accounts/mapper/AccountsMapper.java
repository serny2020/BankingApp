package com.microservices.accounts.mapper;

import com.microservices.accounts.dto.AccountsDto;
import com.microservices.accounts.entity.Accounts;


/**
 * Utility class to map between Accounts entity and AccountsDto.
 * Provides static methods for conversion.
 */
public class AccountsMapper {

    /**
     * Converts an Accounts entity into an AccountsDto object.
     * This method copies account details from the entity to the DTO.
     *
     * @param accounts    The source Accounts entity.
     * @param accountsDto The target AccountsDto object to populate.
     * @return The populated AccountsDto object.
     */
    public static AccountsDto mapToAccountsDto(Accounts accounts, AccountsDto accountsDto) {
        // Map account details from entity to DTO
        accountsDto.setAccountNumber(accounts.getAccountNumber());
        accountsDto.setAccountType(accounts.getAccountType());
        accountsDto.setBranchAddress(accounts.getBranchAddress());
        return accountsDto;
    }

    /**
     * Converts an AccountsDto object into an Accounts entity.
     * This method copies account details from the DTO to the entity.
     *
     * @param accountsDto The source AccountsDto object.
     * @param accounts    The target Accounts entity to populate.
     * @return The populated Accounts entity.
     */
    public static Accounts mapToAccounts(AccountsDto accountsDto, Accounts accounts) {
        // Map account details from DTO to entity
        accounts.setAccountNumber(accountsDto.getAccountNumber());
        accounts.setAccountType(accountsDto.getAccountType());
        accounts.setBranchAddress(accountsDto.getBranchAddress());
        return accounts;
    }
}

