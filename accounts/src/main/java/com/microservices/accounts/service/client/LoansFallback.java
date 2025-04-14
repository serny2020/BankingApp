package com.microservices.accounts.service.client;

import com.microservices.accounts.dto.LoansDto;
import com.microservices.accounts.service.client.LoansFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LoansFallback implements LoansFeignClient {

    @Override
    public ResponseEntity<LoansDto> fetchLoanDetails(String correlationId, String mobileNumber) {
        return null;
    }
}