package com.pilot.ticket.controller;

import com.pilot.ticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket/v1")
public class TicketController {
    @Autowired
    private TicketService ticketService;
    @PutMapping("/buy")
    public ResponseEntity<?> buyTicket(@RequestParam(name = "id") String userId){
        return ResponseEntity.ok(ticketService.buyTicket(userId));
    }


    @DeleteMapping("/cancel")
    public ResponseEntity<?> cancelTicket(@RequestParam(name = "user") String userId,
                                          @RequestParam(name = "ticket") String ticketId){
        return ResponseEntity.ok(ticketService.cancelTicket(userId, ticketId));
    }

    @GetMapping("/history")
    public ResponseEntity<?> getHistory(){
        return ResponseEntity.ok(ticketService.getHistory());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTicket() {return ResponseEntity.ok(ticketService.addTicket());}
}
