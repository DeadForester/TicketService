package dev.ticketManager.services;

import dev.ticketManager.repositories.ticket.TicketRepository;
import dev.ticketManager.web.dto.TicketDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;

    public Flux<TicketDto> getAllTicket() {
        return ticketRepository.getAllTicket();
    }

    public Flux<TicketDto> getAvailableTickets(String departure, String destination,
                                               String carrierName, String fromDate,
                                               String toDate, int page, int size) {
        return ticketRepository.getAvailableTickets(departure, destination, carrierName, fromDate, toDate, page, size);
    }

    public Mono<Boolean> buyTicket(Long ticketId, Long userId) {
        return ticketRepository.buyTicket(ticketId, userId);
    }

    public Flux<TicketDto> getUserTickets(Long userId) {
        return ticketRepository.getUserTickets(userId);
    }
}