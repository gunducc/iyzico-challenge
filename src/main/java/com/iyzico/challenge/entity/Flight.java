package com.iyzico.challenge.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "flights")
public class Flight {
    @Id
    @GeneratedValue
    private Long id;
    private String flightNumber;
    private String departure;
    private String arrival;
    private int seatsLeft;
    private int maxSeats;
    private BigDecimal seatPrice;

}
