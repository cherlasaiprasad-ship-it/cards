package com.example.demo.controller;

import com.example.demo.model.Card;
import com.example.demo.service.CardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/admin/cards")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCardController {
	private final CardService cardService;

	private static final Logger log = LoggerFactory.getLogger(AdminCardController.class);

	public AdminCardController(CardService cardService) {
		this.cardService = cardService;
	}

	@GetMapping("")
	public String getAllCards(@RequestParam(value = "status", required = false) String status, Model model) {
		List<Card> cards;
		log.info("AdminCardController.getAllCards called with status={}", status);
		if (status != null) {
			try {
				// Allow a special "newly" status which means cards that are neither ACTIVE nor PENDING
				if ("NEWLY".equalsIgnoreCase(status) || "NEWLY_APPLIED".equalsIgnoreCase(status) || "OTHER".equalsIgnoreCase(status)) {
					cards = cardService.getAllCards();
					cards = cards.stream()
						.filter(c -> c.getStatus() != Card.Status.ACTIVE && c.getStatus() != Card.Status.PENDING)
						.toList();
					model.addAttribute("filterStatus", "NEWLY APPLIED");
				} else {
					Card.Status s = Card.Status.valueOf(status.toUpperCase());
					cards = cardService.getCardsByStatus(s);
					model.addAttribute("filterStatus", s.name());
				}
			} catch (IllegalArgumentException e) {
				cards = cardService.getAllCards();
			}
		} else {
			cards = cardService.getAllCards();
		}
		model.addAttribute("cards", cards);
		log.info("Returning {} cards for status={}", cards != null ? cards.size() : 0, status);
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