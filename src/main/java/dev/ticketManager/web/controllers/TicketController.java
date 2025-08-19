package dev.ticketManager.web.controllers;

import dev.ticketManager.services.TicketService;
import dev.ticketManager.web.dto.TicketDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/tickets")
@Tag(name = "Tickets", description = "Операции с билетами")
public class TicketController {

    private final TicketService ticketService;

    @GetMapping
    @Operation(summary = "Получить все билеты", description = "Возвращает список всех доступных билетов без владельца")
    public Mono<ResponseEntity<List<TicketDto>>> getAllTicket() {
        return ticketService.getAllTicket()
                .collectList()
                .flatMap(list -> list.isEmpty()
                        ? Mono.just(ResponseEntity.noContent().build())
                        : Mono.just(ResponseEntity.ok(list)));
    }

    @GetMapping("/available")
    @Operation(summary = "Получить доступные билеты", description = "Список билетов с пагинацией и фильтрацией")
    public Mono<ResponseEntity<List<TicketDto>>> getAvailableTickets(
            @Parameter(description = "Пункт отправления") @RequestParam(required = false) String departure,
            @Parameter(description = "Пункт назначения") @RequestParam(required = false) String destination,
            @Parameter(description = "Название перевозчика") @RequestParam(required = false) String carrier,
            @Parameter(description = "Дата/время c") @RequestParam(required = false) String fromDate,
            @Parameter(description = "Дата/время по ") @RequestParam(required = false) String toDate,
            @Parameter(description = "Номер страницы") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "10") int size) {

        if (page < 0) {
            throw new IllegalArgumentException("page должен быть >= 0");
        }
        if (size <= 0 || size > 100) {
            throw new IllegalArgumentException("size должен быть > 0 и <= 100");
        }

        return ticketService.getAvailableTickets(departure, destination, carrier, fromDate, toDate, page, size)
                .collectList()
                .flatMap(list -> list.isEmpty()
                        ? Mono.just(ResponseEntity.noContent().build())
                        : Mono.just(ResponseEntity.ok(list)));
    }


    @PostMapping("/{ticketId}/buy")
    @Operation(summary = "Купить билет", description = "Помечает билет как купленный пользователем")
    public Mono<ResponseEntity<String>> buyTicket(@PathVariable Long ticketId,
                                                  @RequestParam Long userId) {
        return ticketService.buyTicket(ticketId, userId)
                .map(success -> success
                        ? ResponseEntity.ok("Билет успешно куплен")
                        : ResponseEntity.status(HttpStatus.CONFLICT).body("Билет уже куплен"));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Список билетов пользователя", description = "Возвращает все купленные билеты указанного пользователя")
    public Mono<ResponseEntity<List<TicketDto>>> getUserTickets(@PathVariable Long userId) {
        return ticketService.getUserTickets(userId)
                .collectList()
                .flatMap(list -> list.isEmpty()
                        ? Mono.just(ResponseEntity.noContent().build())
                        : Mono.just(ResponseEntity.ok(list)));
    }
}
