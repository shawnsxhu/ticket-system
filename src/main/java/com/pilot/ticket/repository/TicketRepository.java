package com.pilot.ticket.repository;

import com.pilot.ticket.entity.TicketStatus;
import com.pilot.ticket.entity.User;
import com.pilot.ticket.pojo.TicketItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TicketRepository extends MongoRepository<TicketItem, String> {

    public TicketItem findByUser(User user);

    public List<TicketItem> findByTicketStatus(TicketStatus ticketStatus);

    public long count();
}