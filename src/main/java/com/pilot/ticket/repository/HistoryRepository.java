package com.pilot.ticket.repository;

import com.pilot.ticket.entity.Status;
import com.pilot.ticket.entity.TicketStatus;
import com.pilot.ticket.entity.User;
import com.pilot.ticket.pojo.HistoryItem;
import com.pilot.ticket.pojo.TicketItem;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;
import java.util.List;

public interface HistoryRepository extends MongoRepository<HistoryItem, String> {
    public List<HistoryItem> findByStatus(Status ticketStatus);
    public HistoryItem findByUser(User user);

    public long count();
}
