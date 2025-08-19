package dev.ticketManager.repositories.ticket;

import dev.ticketManager.web.dto.CarrierDto;
import dev.ticketManager.web.dto.RouteDto;
import dev.ticketManager.web.dto.TicketDto;
import io.r2dbc.spi.Row;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DaoTicketRepository implements TicketRepository {

    private final DatabaseClient databaseClient;

    private static final String BASE_SELECT = """
            select t.ticket_id
                   , t.local_date_time
                   , t.seat_number
                   , t.price
                   , r.route_id
                   , r.point_of_departure
                   , r.destination
                   , r.travel_time
                   , c.carrier_id
                   , c.name as carrier_name
                   , c.telephone_number
            from ticket t
            join route r on t.route_id = r.route_id
            join carrier c on r.carrier_id = c.carrier_id
            """;

    private static final String BUY_TICKET = """
            update ticket
            set user_data_id = :userId
            where ticket_id = :ticketId and user_data_id is null
            """;

    @Override
    public Flux<TicketDto> getAllTicket() {
        return databaseClient.sql(BASE_SELECT)
                .map((row, meta) -> mapRow(row))
                .all();
    }

    @Override
    public Flux<TicketDto> getAvailableTickets(String departure, String destination,
                                               String carrierName, String fromDate,
                                               String toDate, int page, int size) {
        StringBuilder sb = new StringBuilder(BASE_SELECT);
        sb.append(" where t.user_data_id is null ");

        if (fromDate != null && !fromDate.trim().isEmpty()) {
            sb.append(" and t.local_date_time >= :fromDate ");
        }
        if (toDate != null && !toDate.trim().isEmpty()) {
            sb.append(" and t.local_date_time <= :toDate ");
        }
        if (departure != null && !departure.trim().isEmpty()) {
            sb.append(" and lower(r.point_of_departure) like lower(concat('%', :departure, '%')) ");
        }
        if (destination != null && !destination.trim().isEmpty()) {
            sb.append(" and lower(r.destination) like lower(concat('%', :destination, '%')) ");
        }
        if (carrierName != null && !carrierName.trim().isEmpty()) {
            sb.append(" and lower(c.name) like lower(concat('%', :carrier, '%')) ");
        }

        sb.append(" order by t.local_date_time asc ");
        sb.append(" limit :size offset :offset");

        DatabaseClient.GenericExecuteSpec spec = databaseClient.sql(sb.toString());

        if (fromDate != null && !fromDate.trim().isEmpty()) {
            spec = spec.bind("fromDate", LocalDateTime.parse(fromDate));
        }
        if (toDate != null && !toDate.trim().isEmpty()) {
            spec = spec.bind("toDate", LocalDateTime.parse(toDate));
        }
        if (departure != null && !departure.trim().isEmpty()) {
            spec = spec.bind("departure", departure.trim());
        }
        if (destination != null && !destination.trim().isEmpty()) {
            spec = spec.bind("destination", destination.trim());
        }
        if (carrierName != null && !carrierName.trim().isEmpty()) {
            spec = spec.bind("carrier", carrierName.trim());
        }

        spec = spec.bind("size", size)
                .bind("offset", (long) page * size);

        return spec.map((row, meta) -> mapRow(row)).all();
    }

    @Override
    public Mono<Boolean> buyTicket(Long ticketId, Long userId) {


        return databaseClient.sql(BUY_TICKET)
                .bind("userId", userId)
                .bind("ticketId", ticketId)
                .fetch()
                .rowsUpdated()
                .map(count -> count > 0);
    }

    @Override
    public Flux<TicketDto> getUserTickets(Long userId) {
        String sql = BASE_SELECT + " where t.user_data_id = :userId";
        return databaseClient.sql(sql)
                .bind("userId", userId)
                .map((row, meta) -> mapRow(row))
                .all();
    }

    private TicketDto mapRow(Row row) {
        CarrierDto carrier = new CarrierDto(
                row.get("carrier_name", String.class),
                row.get("telephone_number", String.class)
        );

        RouteDto route = new RouteDto(
                row.get("point_of_departure", String.class),
                row.get("destination", String.class),
                carrier
        );

        return new TicketDto(
                row.get("ticket_id", Long.class),
                route,
                row.get("local_date_time", LocalDateTime.class),
                row.get("seat_number", Integer.class),
                row.get("price", Double.class)
        );
    }
}