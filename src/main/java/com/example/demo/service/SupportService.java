package com.example.demo.service;

import com.example.demo.model.SupportTicket;
import com.example.demo.repository.SupportTicketRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class SupportService {
    private final SupportTicketRepository supportTicketRepository;
    private final com.example.demo.repository.UserRepository userRepository;

    public SupportService(SupportTicketRepository supportTicketRepository,
            com.example.demo.repository.UserRepository userRepository) {
        this.supportTicketRepository = supportTicketRepository;
        this.userRepository = userRepository;
    }

    public SupportTicket createTicket(SupportTicket ticket) {
        if (ticket.getUser() != null && ticket.getUser().getUserId() != null) {
            com.example.demo.model.User user = userRepository.findById(ticket.getUser().getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            ticket.setUser(user);
        }
        ticket.setCreatedDate(LocalDateTime.now());
        ticket.setTicketStatus(SupportTicket.Status.OPEN);
        return supportTicketRepository.save(ticket);
    }
}
