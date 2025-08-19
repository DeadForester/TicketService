package dev.ticketManager.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarrierDto {
    private String name;
    private String telephoneNumber;
}
