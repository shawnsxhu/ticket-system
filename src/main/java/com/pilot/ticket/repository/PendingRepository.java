package com.pilot.ticket.repository;

import com.pilot.ticket.pojo.PendingTicket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PendingRepository extends MongoRepository<PendingTicket, String> {
    List<PendingTicket> findByOrderByCreationTimeAsc();
}
