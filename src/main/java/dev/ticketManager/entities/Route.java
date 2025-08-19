package dev.ticketManager.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table("route")
public class Route {
    @Id
    private int id;

    private String pointOfDeparture;

    private String destination;

    private Carrier carrier;

    private double travelTime;
}
