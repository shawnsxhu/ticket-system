package com.pilot.ticket.pojo;

import com.pilot.ticket.entity.Status;
import com.pilot.ticket.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("historyitem")
@Getter
@Setter
@ToString
public class HistoryItem {
    @Id
    private String id;
    private TicketItem ticketItem;
    private User user;
    private Status status;

    public HistoryItem(TicketItem ticketItem, User user, Status status){
        super();
        this.ticketItem = ticketItem;
        this.user = user;
        this.status = status;
    }
}
