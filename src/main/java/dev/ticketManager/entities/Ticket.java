package dev.ticketManager.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table("ticket")
public class Ticket {
    @Id
    private Long id;

    private Route route;

    private User user;

    private LocalDateTime localDateTime;

    private Integer seatNumber;

    private Double price;
}
