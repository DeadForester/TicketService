package dev.ticketManager.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDto {
    private Long id;
    private RouteDto route;
    private LocalDateTime localDateTime;
    private Integer seatNumber;
    private Double price;
}
