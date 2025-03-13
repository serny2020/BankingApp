package com.microservices.accounts.controller;

import com.microservices.accounts.dto.CustomerDto;
import com.microservices.accounts.dto.ResponseDto;
import com.microservices.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.microservices.accounts.constants.AccountsConstants;
import org.springframework.web.bind.annotation.*;

/**
 * AccountsController handles the API endpoints for account-related operations.
 * It exposes endpoints to manage customer accounts, such as creating a new account.
 * This controller interacts with the service layer to perform the business logic.
 */
@RestController
@RequestMapping(path="/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class AccountsController {

    // The service responsible for account-related operations
    private IAccountsService iAccountsService;

    /**
     * Endpoint to create a new customer account.
     * It takes a CustomerDto, maps the data, and calls the service to perform the account creation.
     *
     * @param customerDto The data transfer object containing the customer details.
     * @return A ResponseEntity containing the response status and message.
     */
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@RequestBody CustomerDto customerDto) {
        // Calls the service layer to create the customer account
        iAccountsService.createAccount(customerDto);

        // Returns a response with HTTP status 201 (Created) and a success message
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }

}

//@RestController
//public class AccountsController {
//    @GetMapping("sayHello")
//    public String sayHello() {
//        return "Hi from AccountsController!";
//    }
//}
