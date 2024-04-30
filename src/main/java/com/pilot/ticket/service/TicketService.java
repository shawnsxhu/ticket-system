package com.pilot.ticket.service;

import com.pilot.ticket.entity.Status;
import com.pilot.ticket.entity.TicketStatus;
import com.pilot.ticket.entity.User;
import com.pilot.ticket.pojo.HistoryItem;
import com.pilot.ticket.pojo.PendingTicket;
import com.pilot.ticket.pojo.TicketItem;
import com.pilot.ticket.repository.HistoryRepository;
import com.pilot.ticket.repository.PendingRepository;
import com.pilot.ticket.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PendingRepository pendingRepository;

    public String buyTicket(String userId){
        List<TicketItem> availableTickets = ticketRepository.findByTicketStatus(TicketStatus.AVAILABLE);
        if(availableTickets != null && !availableTickets.isEmpty()){
            User ticketUser = new User(userId);
            TicketItem newTicket = availableTickets.get(0);
            historyRepository.save(new HistoryItem(newTicket, ticketUser, Status.CONFIRMED));
            ticketRepository.delete(newTicket);
            newTicket.setTicketStatus(TicketStatus.BOOKED);
            newTicket.setUser(ticketUser);
            ticketRepository.save(newTicket);
            return String.format("Ticket %s confirmed", newTicket.getId());
        }else{
            User pendingUser = new User(userId);
            pendingRepository.save(new PendingTicket(pendingUser, System.currentTimeMillis()));
            historyRepository.save(new HistoryItem(null, pendingUser, Status.PENDING));
            return "Ticket pending";
        }
    }

    public List<HistoryItem> getHistory(){
        return historyRepository.findAll();
    }

    public String cancelTicket(String userId, String ticketId){
        Optional<TicketItem> ticket = ticketRepository.findById(ticketId);
        if(ticket.isPresent() && ticket.get().getUser().getId().equals(userId)) {
            historyRepository.save(new HistoryItem(ticket.get(),
                    ticket.get().getUser(),
                    Status.CANCELLED));
            List<PendingTicket> pendingTickets = pendingRepository.findByOrderByCreationTimeAsc();
            if(pendingTickets != null && !pendingTickets.isEmpty()){
                PendingTicket nextInLine = pendingTickets.get(0);
                historyRepository.save(new HistoryItem(ticket.get(),
                        nextInLine.getUser(),
                        Status.CONFIRMED));
                ticketRepository.delete(ticket.get());
                TicketItem newTicket = ticket.get();
                newTicket.setUser(nextInLine.getUser());
                newTicket.setTicketStatus(TicketStatus.BOOKED);
                ticketRepository.save(newTicket);
                pendingRepository.delete(nextInLine);
            }else{
                ticketRepository.delete(ticket.get());
                TicketItem newTicket = ticket.get();
                newTicket.setUser(null);
                newTicket.setTicketStatus(TicketStatus.AVAILABLE);
                ticketRepository.save(newTicket);
            }
            return String.format("Ticket %s cancelled", ticket.get().getId());
        }else {
            return "Ticket does not exist";
        }
    }

    public String addTicket(){
        TicketItem newTicket = new TicketItem(null, TicketStatus.AVAILABLE);
        this.ticketRepository.save(newTicket);
        return newTicket.getId();
    }

    @Scheduled(cron = "*/10 * * * * *")
    public void cancelExpiredTicket() {
        List<PendingTicket> pendingTickets = pendingRepository.findByOrderByCreationTimeAsc();
        List<TicketItem> availableTickets = ticketRepository.findByTicketStatus(TicketStatus.AVAILABLE);
        availableTickets.forEach(ticket -> {
            if(pendingTickets != null && !pendingTickets.isEmpty()){
                PendingTicket nextInLine = pendingTickets.get(0);
                historyRepository.save(new HistoryItem(ticket,
                        nextInLine.getUser(),
                        Status.CONFIRMED));
                ticketRepository.delete(ticket);
                ticket.setUser(nextInLine.getUser());
                ticket.setTicketStatus(TicketStatus.BOOKED);
                ticketRepository.save(ticket);
                pendingTickets.remove(0);
                pendingRepository.delete(nextInLine);
            }
        });
        pendingTickets.forEach(ticket -> {
            if (ticket.getCreationTime() + 5 * 1000 * 60 >= System.currentTimeMillis()) {
                historyRepository.save(new HistoryItem(null,
                        ticket.getUser(),
                        Status.CANCELLED));
                pendingRepository.deleteById(ticket.getId());
            }
        });
    }
}
