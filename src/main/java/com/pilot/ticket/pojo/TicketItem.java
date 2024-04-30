package com.pilot.ticket.pojo;

import com.pilot.ticket.entity.Status;
import com.pilot.ticket.entity.TicketStatus;
import com.pilot.ticket.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("ticketitems")
@Getter
@Setter
@ToString
public class TicketItem {
    @Id
    private String id;

    private User user;

    private TicketStatus ticketStatus;

    public TicketItem(User user, TicketStatus ticketStatus) {
        super();
        this.user = user;
        this.ticketStatus = ticketStatus;
    }
}
