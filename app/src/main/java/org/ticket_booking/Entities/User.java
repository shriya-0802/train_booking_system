package org.ticket_booking.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;
import org.ticket_booking.Entities.Ticket;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class) // Fixed the constant issue
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String name;
    private String password;
    private String hashedPassword;
    private List<Ticket> ticketsBooked;
    private String userId;

    // Keeping printTickets() function
    public void printTickets() {
        if (ticketsBooked != null && !ticketsBooked.isEmpty()) {
            for (Ticket ticket : ticketsBooked) {
                System.out.println(ticket.getTicketInfo());
            }
        } else {
            System.out.println("No tickets booked.");
        }
    }
}
