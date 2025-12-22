package com.example.demo.controller;

import com.example.demo.model.Transaction;
import com.example.demo.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final com.example.demo.service.CardService cardService;

    public TransactionController(TransactionService transactionService,
            com.example.demo.service.CardService cardService) {
        this.transactionService = transactionService;
        this.cardService = cardService;
    }

    @GetMapping
    public String transactionHistory(@RequestParam(required = false) Long cardId, Model model) {
        if (cardId != null) {
            model.addAttribute("transactions", transactionService.getTransactionHistory(cardId));
        }
        return "transactions";
    }

    @GetMapping("/new")
    public String newTransactionForm(Model model) {
        model.addAttribute("transaction", new Transaction());
        model.addAttribute("cards", cardService.getAllCards());
        return "new_transaction";
    }

    @PostMapping("/new")
    public String makeTransaction(@ModelAttribute Transaction transaction, Model model) {
        try {
            transactionService.makeTransaction(transaction);
            return "redirect:/transactions?cardId=" + transaction.getCard().getCardId();
        } catch (RuntimeException e) {
            model.addAttribute("error", "Error: " + e.getMessage());
            model.addAttribute("transaction", transaction);
            model.addAttribute("cards", cardService.getAllCards());
            return "new_transaction";
        }
    }
}
