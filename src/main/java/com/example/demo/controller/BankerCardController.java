package com.example.demo.controller;

import com.example.demo.service.CardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@RequestMapping("/banker/cards")
@PreAuthorize("hasRole('BANKER')")
public class BankerCardController {
    private final CardService cardService;

    public BankerCardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("")
    public String getAllCards(Model model) {
        model.addAttribute("cards", cardService.getAllCards());
        return "banker_cards";
    }

    @PostMapping("/approve/{id}")
    public String approveCard(@PathVariable Long id) {
        cardService.approveCard(id);
        return "redirect:/banker/cards";
    }

    @PostMapping("/limit/{id}")
    public String updateCreditLimit(@PathVariable Long id, @RequestParam java.math.BigDecimal amount) {
        cardService.updateCreditLimit(id, amount);
        return "redirect:/banker/cards";
    }

    @GetMapping("/details")
    public String getCardDetails(Model model) {
        model.addAttribute("cards", cardService.getAllCards());
        return "card_details";
    }
}