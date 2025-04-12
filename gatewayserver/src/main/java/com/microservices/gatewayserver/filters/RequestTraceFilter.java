package com.microservices.gatewayserver.filters;

import com.microservices.gatewayserver.filters.FilterUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Order(1) // This annotation specifies that this filter should be executed first in the filter chain
@Component // Marks this class as a Spring component so it's picked up during component scanning
public class RequestTraceFilter implements GlobalFilter {

    // Logger to output debug messages for tracing and troubleshooting
    private static final Logger logger = LoggerFactory.getLogger(RequestTraceFilter.class);

    // Injecting a utility class to help manage correlation IDs
    @Autowired
    FilterUtility filterUtility;

    // The main filter method that is executed for every incoming HTTP request
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Get the request headers
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();

        // Check if the request already contains a correlation ID
        if (isCorrelationIdPresent(requestHeaders)) {
            // If present, log the existing correlation ID
            logger.debug("Bank-correlation-id found in RequestTraceFilter : {}",
                    filterUtility.getCorrelationId(requestHeaders));
        } else {
            // If not present, generate a new correlation ID
            String correlationID = generateCorrelationId();
            // Add the new correlation ID to the request headers
            exchange = filterUtility.setCorrelationId(exchange, correlationID);
            // Log the newly generated correlation ID
            logger.debug("Bank-correlation-id generated in RequestTraceFilter : {}", correlationID);
        }

        // Continue processing the request with the next filter in the chain
        return chain.filter(exchange);
    }

    // Helper method to check if the correlation ID is already present in the headers
    private boolean isCorrelationIdPresent(HttpHeaders requestHeaders) {
        return filterUtility.getCorrelationId(requestHeaders) != null;
    }

    // Helper method to generate a new unique correlation ID using UUID
    private String generateCorrelationId() {
        return java.util.UUID.randomUUID().toString();
    }
}
