package com.example.demo.service;

import com.example.demo.model.Bill;
import com.example.demo.repository.BillRepository;
import org.springframework.stereotype.Service;

@Service
public class BillingService {
    private final BillRepository billRepository;
    private final com.example.demo.repository.CardRepository cardRepository;

    public BillingService(BillRepository billRepository, com.example.demo.repository.CardRepository cardRepository) {
        this.billRepository = billRepository;
        this.cardRepository = cardRepository;
    }

    public Bill generateBill(Bill bill) {
        if (bill.getCard() != null && bill.getCard().getCardId() != null) {
            com.example.demo.model.Card card = cardRepository.findById(bill.getCard().getCardId())
                    .orElseThrow(() -> new RuntimeException("Card not found"));
            bill.setCard(card);
        }
        bill.setStatus(Bill.Status.PENDING); // Default
        return billRepository.save(bill);
    }

    public java.util.Optional<Bill> getBillById(Long id) {
        return billRepository.findById(id);
    }
}
