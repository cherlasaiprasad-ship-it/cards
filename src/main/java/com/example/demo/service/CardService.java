package com.example.demo.service;

import com.example.demo.model.Card;
import com.example.demo.model.User;
import com.example.demo.repository.CardRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

@Service
public class CardService {
    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Card applyCard(Card card) {
        card.setStatus(Card.Status.PENDING); // Default to PENDING
        return cardRepository.save(card);
    }

    public Card approveCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        if (card.getStatus() != Card.Status.PENDING) {
            throw new RuntimeException("Only cards with status PENDING can be approved");
        }
        // Generate unique 16-digit card number based on card type
        String prefix = switch (card.getCardType()) {
            case Visa -> "4";
            case MasterCard -> "5";
            case RuPay -> "6";
        };
        SecureRandom random = new SecureRandom();
        Set<String> existingNumbers = new HashSet<>();
        cardRepository.findAll().forEach(c -> existingNumbers.add(c.getCardNumber()));
        String generatedNumber;
        do {
            StringBuilder sb = new StringBuilder(prefix);
            for (int i = 0; i < 15; i++) {
                sb.append(random.nextInt(10));
            }
            generatedNumber = sb.toString();
        } while (existingNumbers.contains(generatedNumber));
        card.setCardNumber(generatedNumber);
        card.setStatus(Card.Status.ACTIVE);
        card.setAvailableBalance(card.getCreditLimit());
        return cardRepository.save(card);
    }

    public Card updateCreditLimit(Long cardId, java.math.BigDecimal newLimit) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));
        if (card.getStatus() != Card.Status.ACTIVE) {
            throw new RuntimeException("Credit limit can only be updated for ACTIVE cards");
        }
        if (newLimit == null || newLimit.compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Credit limit must be greater than zero");
        }
        // Academic logic: update availableBalance by the difference
        java.math.BigDecimal diff = newLimit.subtract(card.getCreditLimit());
        card.setCreditLimit(newLimit);
        card.setAvailableBalance(card.getAvailableBalance().add(diff));
        return cardRepository.save(card);
    }

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    public Optional<Card> getCardDetails(Long cardId) {
        return cardRepository.findById(cardId);
    }

    public List<Card> getCardsByUser(User user) {
        return cardRepository.findByUser(user);
    }

    public List<Card> getCardsByStatus(Card.Status status) {
        return cardRepository.findByStatus(status);
    }
}