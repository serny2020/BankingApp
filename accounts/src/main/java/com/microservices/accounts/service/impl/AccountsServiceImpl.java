package com.microservices.accounts.service.impl;

import com.microservices.accounts.constants.AccountsConstants;
import com.microservices.accounts.dto.AccountsDto;
import com.microservices.accounts.dto.CustomerDto;
import com.microservices.accounts.entity.Customer;
import com.microservices.accounts.exception.CustomerAlreadyExistsException;
import com.microservices.accounts.exception.ResourceNotFoundException;
import com.microservices.accounts.mapper.AccountsMapper;
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

    /**
     * Creates a new customer account if the provided mobile number is not already registered.
     *
     * @param customerDto The customer data transfer object containing the details of the customer to be registered.
     * @throws CustomerAlreadyExistsException If a customer with the given mobile number already exists.
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        // Map the CustomerDto to a Customer entity using the CustomerMapper
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());

        // Check if a customer with the given mobile number already exists in the repository
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if (optionalCustomer.isPresent()) {
            // Throw an exception if the customer already exists
            throw new CustomerAlreadyExistsException("Customer already registered with given mobile number "
                    + customerDto.getMobileNumber());
        }

        // Set creation metadata for the new customer
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Anonymous");

        // Save the new customer entity in the customer repository
        Customer savedCustomer = customerRepository.save(customer);

        // Create and save a new account associated with the newly created customer
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

    /**
     * @param mobileNumber - Input Mobile Number
     * @return Accounts Details based on a given mobileNumber
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );
        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
        return customerDto;
    }

    /**
     * @param customerDto - CustomerDto Object
     * @return boolean indicating if the update of Account details is successful or not
     */
    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;

        // Retrieve the accounts DTO from the customer DTO
        AccountsDto accountsDto = customerDto.getAccountsDto();

        if (accountsDto != null) {
            // Fetch the existing account from the database using the account number
            // If the account does not exist, throw a ResourceNotFoundException
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );

            // Map the updated account details from accountsDto to the existing account entity
            // This ensures that only the existing account is updated instead of creating a new one
            AccountsMapper.mapToAccounts(accountsDto, accounts);

            // Save the updated account entity to persist changes
            // This performs an update operation since the entity already exists in the repository
            accounts = accountsRepository.save(accounts);

            // Retrieve the customer ID associated with the account
            Long customerId = accounts.getCustomerId();

            // Fetch the existing customer using the customer ID
            // If the customer does not exist, throw a ResourceNotFoundException
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );

            // Map the updated customer details from customerDto to the existing customer entity
            // This ensures that only the existing customer is updated instead of creating a new one
            CustomerMapper.mapToCustomer(customerDto, customer);

            // Save the updated customer entity to persist changes
            // This also performs an update operation since the entity already exists
            customerRepository.save(customer);

            // Mark the update as successful
            isUpdated = true;
        }

        return isUpdated;
    }

    /**
     * @param mobileNumber - Input Mobile Number
     * @return boolean indicating if the delete of Account details is successful or not
     */
    @Override
    public boolean deleteAccount(String mobileNumber) {
        // Retrieve the customer entity using the mobile number
        // If the customer is not found, throw a ResourceNotFoundException
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );

        // Delete all accounts associated with the customer ID
        // This ensures that account records are removed before deleting the customer
        // Since customerId is not primary key, use the defined method to find item here
        accountsRepository.deleteByCustomerId(customer.getCustomerId());

        // Delete the customer record from the repository
        customerRepository.deleteById(customer.getCustomerId());

        // Return true to indicate successful deletion
        return true;
    }
}


