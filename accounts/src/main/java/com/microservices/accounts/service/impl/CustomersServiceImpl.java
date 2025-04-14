package com.microservices.accounts.service.impl;

import com.microservices.accounts.dto.AccountsDto;
import com.microservices.accounts.dto.CardsDto;
import com.microservices.accounts.dto.CustomerDetailsDto;
import com.microservices.accounts.dto.LoansDto;
import com.microservices.accounts.entity.Accounts;
import com.microservices.accounts.entity.Customer;
import com.microservices.accounts.exception.ResourceNotFoundException;
import com.microservices.accounts.mapper.AccountsMapper;
import com.microservices.accounts.mapper.CustomerMapper;
import com.microservices.accounts.repository.AccountsRepository;
import com.microservices.accounts.repository.CustomerRepository;
import com.microservices.accounts.service.ICustomersService;
import com.microservices.accounts.service.client.CardsFeignClient;
import com.microservices.accounts.service.client.LoansFeignClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CustomersServiceImpl implements ICustomersService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Customer Details based on a given mobileNumber
     */
    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        // Fetch loan details using Feign client with correlation ID for traceability
        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(correlationId, mobileNumber);

        // If the loan service returns a non-null response, update the customer's loan information
        if (null != loansDtoResponseEntity) {
            customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());
        }

        // Fetch card details using Feign client with correlation ID for traceability
        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(correlationId, mobileNumber);

        // If the card service returns a non-null response, update the customer's card information
        if (null != cardsDtoResponseEntity) {
            customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());
        }


        return customerDetailsDto;
    }
}