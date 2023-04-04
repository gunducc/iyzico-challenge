package com.iyzico.challenge.service;

import com.iyzico.challenge.dto.ReservationDto;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Reservation;

public interface ReservationService {

    public Reservation getReservation(long id);

    public Flight getFlightFromReservation(long id);

    public ReservationDto makeReservation(Long passengerId, Long flightId);

    public ReservationDto cancelReservation(String pnr);

    public ReservationDto updateReservationStatus(String status, long reservationId);



}
