package dev.ticketManager.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteDto {
    private String pointOfDeparture;
    private String destination;
    private CarrierDto carrier;
}
