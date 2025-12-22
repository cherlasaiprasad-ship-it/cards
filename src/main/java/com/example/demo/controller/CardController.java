package com.example.demo.controller;

import com.example.demo.model.Card;
import com.example.demo.service.CardService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Controller
@RequestMapping("/cards")
public class CardController {
    private final CardService cardService;
    private final com.example.demo.service.AuthService authService;

    public CardController(CardService cardService, com.example.demo.service.AuthService authService) {
        this.cardService = cardService;
        this.authService = authService;
    }

    @GetMapping
    public String getAllCards(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            String username = userDetails.getUsername();
            com.example.demo.model.User user = authService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            model.addAttribute("cards", cardService.getCardsByUser(user));
        } else {
            model.addAttribute("cards", List.of());
        }
        return "cards";
    }

    @GetMapping("/apply")
    public String applyCardForm(Model model) {
        model.addAttribute("card", new Card());
        return "apply_card";
    }

    @PostMapping("/apply")
    public String applyCard(@ModelAttribute Card card, java.security.Principal principal, Model model) {
        try {
            String username = principal.getName();
            com.example.demo.model.User user = authService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            card.setUser(user);
            cardService.applyCard(card);
            return "redirect:/cards";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Error: " + e.getMessage());
            model.addAttribute("card", card);
            return "apply_card";
        }
    }

    @PreAuthorize("hasRole('BANKER')")
    @PostMapping("/approve/{id}")
    public String approveCard(@PathVariable Long id) {
        cardService.approveCard(id);
        return "redirect:/banker/dashboard";
    }

    @PostMapping("/limit/{id}")
    public String updateCreditLimit(@PathVariable Long id, @RequestParam java.math.BigDecimal amount) {
        cardService.updateCreditLimit(id, amount);
        return "redirect:/cards";
    }

    @GetMapping("/details")
    public String getCardDetails(Model model, @AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        if (role.equals("ROLE_ADMIN") || role.equals("ROLE_BANKER")) {
            model.addAttribute("cards", cardService.getAllCards());
        } else {
            String username = userDetails.getUsername();
            com.example.demo.model.User user = authService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
            model.addAttribute("cards", cardService.getCardsByUser(user));
        }
        return "card_details";
    }
}