package com.pilot.ticket.pojo;

import com.pilot.ticket.entity.User;
import com.pilot.ticket.pojo.TicketItem;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("pendingticket")
@Getter
@Setter
@ToString
public class PendingTicket {
    @Id
    private String id;
    private User user;
    private Long creationTime;

    public PendingTicket(User user, Long creationTime) {
        super();
        this.user = user;
        this.creationTime = creationTime;
    }
}
