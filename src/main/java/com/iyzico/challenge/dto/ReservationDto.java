package com.iyzico.challenge.dto;

import lombok.Data;

@Data
public class ReservationDto {
    private String pnr;
    private int seatNumber;
    private String status;
}
