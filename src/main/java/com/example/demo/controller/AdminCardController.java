package com.example.demo.controller;

import com.example.demo.service.CardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@Controller
@RequestMapping("/admin/cards")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCardController {
	private final CardService cardService;

	public AdminCardController(CardService cardService) {
		this.cardService = cardService;
	}

	@GetMapping("")
	public String getAllCards(Model model) {
		model.addAttribute("cards", cardService.getAllCards());
		return "admin_cards";
	}

	@PostMapping("/approve/{id}")
	public String approveCard(@PathVariable Long id) {
		cardService.approveCard(id);
		return "redirect:/admin/cards";
	}

	@PostMapping("/limit/{id}")
	public String updateCreditLimit(@PathVariable Long id, @RequestParam java.math.BigDecimal amount) {
		cardService.updateCreditLimit(id, amount);
		return "redirect:/admin/cards";
	}

	@GetMapping("/details")
	public String getCardDetails(Model model) {
		model.addAttribute("cards", cardService.getAllCards());
		return "card_details";
	}
}