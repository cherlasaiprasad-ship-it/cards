package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin_dashboard";
    }
    @PreAuthorize("hasRole('BANKER')")
    @GetMapping("/banker/dashboard")
    public String bankerDashboard() {
        return "banker_dashboard";
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/customer/dashboard")
    public String customerDashboard() {
        return "abin";
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/customer_dashboard")
    public String customerDashboard2() {
        return "customer_dashboard";
    }
    
}