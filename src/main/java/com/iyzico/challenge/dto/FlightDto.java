package com.iyzico.challenge.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Data
public class FlightDto {
    private long id;
    private String flightNumber;
    private String departure;
    private String arrival;

    @Min(value = 0L, message = "Seat number should be equals or greater than zero")
    private int seatsLeft;

    @Min(value = 0L, message = "Max seat number should be equals or greater than zero")
    private int maxSeats;

    @Min(value = 0, message = "Seat price should be larger than zero")
    private BigDecimal seatPrice;


}
