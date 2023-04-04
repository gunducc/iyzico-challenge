package com.iyzico.challenge.entity;

import lombok.*;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.*;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "reservations")
@OptimisticLocking
public class Reservation {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "passenger_id")
    private Passenger passenger;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    private Long paymentId;
    private int seatNumber;
    private String pnr;
    private String status;

    @Version
    private Integer version;

}
