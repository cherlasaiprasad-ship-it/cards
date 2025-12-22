package com.example.demo.service;

import com.example.demo.model.Transaction;
import com.example.demo.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final com.example.demo.repository.CardRepository cardRepository;

    public TransactionService(TransactionRepository transactionRepository,
            com.example.demo.repository.CardRepository cardRepository) {
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
    }

    public Transaction makeTransaction(Transaction transaction) {
        if (transaction.getCard() != null && transaction.getCard().getCardId() != null) {
            com.example.demo.model.Card card = cardRepository.findById(transaction.getCard().getCardId())
                    .orElseThrow(() -> new RuntimeException("Card not found"));
            transaction.setCard(card);
        }
        transaction.setStatus(Transaction.Status.SUCCESS); // Default
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionHistory(Long cardId) {
        return transactionRepository.findByCard_CardId(cardId);
    }
}
