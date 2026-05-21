package com.paypal.transaction_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paypal.transaction_service.entity.Transaction;
import com.paypal.transaction_service.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository repository;
    private final ObjectMapper objectMapper;

    public TransactionServiceImpl(TransactionRepository repository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Transaction createTransaction(Transaction transaction) {
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setStatus("SUCCESS");

        return repository.save(transaction);
    }

    @Override
    public List<Transaction> getAllTransaction() {
        return repository.findAll();
    }
}
