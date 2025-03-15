package com.microservices.accounts.repository;

import com.microservices.accounts.entity.Accounts;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface AccountsRepository extends JpaRepository<Accounts, Long> {

    Optional<Accounts> findByCustomerId(Long customerId);

    @Transactional  // Ensures the delete operation is part of a transaction, allowing rollback in case of failure.
    @Modifying     // Tell Spring Data JPA this is a customized modifying query (DELETE), not a simple SELECT query.
    void deleteByCustomerId(Long customerId);
}
