package com.microservices.accounts.service.impl;

import com.microservices.accounts.constants.AccountsConstants;
import com.microservices.accounts.dto.CustomerDto;
import com.microservices.accounts.entity.Customer;
import com.microservices.accounts.exception.CustomerAlreadyExistsException;
import com.microservices.accounts.mapper.CustomerMapper;
import com.microservices.accounts.repository.AccountsRepository;
import com.microservices.accounts.repository.CustomerRepository;
import com.microservices.accounts.service.IAccountsService;
import com.microservices.accounts.entity.Accounts;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor // Lombok will generate a constructor with all the arguments
// arguments passed to the constructor are the dependencies that the service needs
public class AccountsServiceImpl implements IAccountsService {

    // The AccountsRepository and CustomerRepository are the dependencies that the service needs
    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    @Override
    public void createAccount(CustomerDto customerDto) {
        // Map the CustomerDto to a Customer entity using the CustomerMapper
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    +customerDto.getMobileNumber());
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Anomymous");
        // Save the mapped Customer entity to the customer repository
        Customer savedCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(savedCustomer));
    }

    /**
     * Creates a new bank account for a given customer.
     * Generates a random account number, sets default values,
     * and initializes the account creation details.
     *
     * @param customer - The customer object for whom the account is being created.
     * @return A newly created Accounts object with assigned details.
     */
    private Accounts createNewAccount(Customer customer) {
        // Create a new Accounts instance
        Accounts newAccount = new Accounts();

        // Set customer ID from the provided Customer object
        newAccount.setCustomerId(customer.getCustomerId());

        // Generate a random 9-digit account number (ensuring it stays within a valid range)
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        // Assign generated account number
        newAccount.setAccountNumber(randomAccNumber);

        // Set default account type (e.g., Savings)
        newAccount.setAccountType(AccountsConstants.SAVINGS);

        // Set default branch address from constants
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);

        // Record the creation timestamp
        newAccount.setCreatedAt(LocalDateTime.now());

        // Set createdBy field to indicate an anonymous system-generated entry
        newAccount.setCreatedBy("Anonymous");

        // Return the newly created account object
        return newAccount;
    }
}
