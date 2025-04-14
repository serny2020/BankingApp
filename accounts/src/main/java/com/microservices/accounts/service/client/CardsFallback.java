package com.microservices.accounts.service.client;

import com.microservices.accounts.dto.CardsDto;
import com.microservices.accounts.service.client.CardsFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CardsFallback implements CardsFeignClient {

    @Override
    public ResponseEntity<CardsDto> fetchCardDetails(String correlationId, String mobileNumber) {
        return null;
    }

}