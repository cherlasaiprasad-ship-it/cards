package com.example.demo.controller;

import com.example.demo.model.SupportTicket;
import com.example.demo.service.SupportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/support")
public class SupportController {
    private final SupportService supportService;

    public SupportController(SupportService supportService) {
        this.supportService = supportService;
    }

    @GetMapping("/create")
    public String createTicketForm(Model model) {
        model.addAttribute("ticket", new SupportTicket());
        return "create_ticket";
    }

    @PostMapping("/create")
    public String createTicket(@ModelAttribute SupportTicket ticket, Model model) {
        try {
            supportService.createTicket(ticket);
            return "redirect:/dashboard";
        } catch (RuntimeException e) {
            model.addAttribute("error", "Error: " + e.getMessage());
            model.addAttribute("ticket", ticket);
            return "create_ticket";
        }
    }
}
