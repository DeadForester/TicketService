package dev.ticketManager.repositories.ticket;

import dev.ticketManager.web.dto.TicketDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TicketRepository {
    Flux<TicketDto> getAllTicket();

    Flux<TicketDto> getAvailableTickets(String departure, String destination,
                                        String carrierName, String fromDate,
                                        String toDate, int page, int size);

    Mono<Boolean> buyTicket(Long ticketId, Long userId);

    Flux<TicketDto> getUserTickets(Long userId);
}
