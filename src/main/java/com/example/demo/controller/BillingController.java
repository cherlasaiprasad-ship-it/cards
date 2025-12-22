package com.example.demo.controller;

import com.example.demo.model.Bill;
import com.example.demo.service.BillingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/billing")
public class BillingController {
    private final BillingService billingService;
    private final com.example.demo.service.CardService cardService;

    public BillingController(BillingService billingService, com.example.demo.service.CardService cardService) {
        this.billingService = billingService;
        this.cardService = cardService;
    }

    @GetMapping("/generate")
    public String generateBillForm(Model model) {
        model.addAttribute("bill", new Bill());
        model.addAttribute("cards", cardService.getAllCards());
        return "generate_bill";
    }

    @PostMapping("/generate")
    public String generateBill(@ModelAttribute Bill bill, Model model) {
        try {
            Bill savedBill = billingService.generateBill(bill);
            return "redirect:/billing/view?billId=" + savedBill.getBillId();
        } catch (RuntimeException e) {
            model.addAttribute("error", "Error: " + e.getMessage());
            model.addAttribute("bill", bill);
            model.addAttribute("cards", cardService.getAllCards());
            return "generate_bill";
        }
    }

    @GetMapping("/view")
    public String viewBillDetails(@RequestParam Long billId, Model model) {
        Bill bill = billingService.getBillById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
        model.addAttribute("bill", bill);
        return "bill_details";
    }

    @GetMapping("/download/{id}")
    @ResponseBody
    public org.springframework.http.ResponseEntity<String> downloadBill(@PathVariable Long id) {
        Bill bill = billingService.getBillById(id)
                .orElseThrow(() -> new RuntimeException("Bill not found"));

        String receipt = "-------- BILL RECEIPT --------\n" +
                "Bill ID: " + bill.getBillId() + "\n" +
                "Card Number: " + bill.getCard().getCardNumber() + "\n" +
                "Billing Date: " + bill.getBillingDate() + "\n" +
                "Due Date: " + bill.getDueDate() + "\n" +
                "Total Amount: " + bill.getTotalAmount() + "\n" +
                "Status: " + bill.getStatus() + "\n" +
                "------------------------------";

        return org.springframework.http.ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"bill_" + id + ".txt\"")
                .body(receipt);
    }
}
