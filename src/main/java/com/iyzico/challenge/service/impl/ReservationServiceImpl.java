package com.iyzico.challenge.service.impl;

import com.iyzico.challenge.dto.FlightDto;
import com.iyzico.challenge.dto.ReservationDto;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Passenger;
import com.iyzico.challenge.entity.Reservation;
import com.iyzico.challenge.exception.ResourceNotFoundException;
import com.iyzico.challenge.repository.FlightRepository;
import com.iyzico.challenge.repository.PassengerRepository;
import com.iyzico.challenge.repository.ReservationRepository;
import com.iyzico.challenge.service.ReservationService;
import org.hibernate.annotations.OptimisticLock;
import org.hibernate.annotations.OptimisticLocking;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.util.Random;

@Service
public class ReservationServiceImpl implements ReservationService {

    private ReservationRepository reservationRepository;

    private FlightRepository flightRepository;

    private PassengerRepository passengerRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository, FlightRepository flightRepository, PassengerRepository passengerRepository) {
        this.reservationRepository = reservationRepository;
        this.flightRepository = flightRepository;
        this.passengerRepository = passengerRepository;
    }


    @Override
    public Reservation getReservation(long id) {
        return reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation","id",id));
    }

    public Flight getFlightFromReservation(long id){
        Reservation reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation","id",id));
        return reservation.getFlight();
    }

    @Override
    @Transactional
    public ReservationDto makeReservation(Long passengerId, Long flightId) {
        Reservation reservation = new Reservation();
        Flight flight = flightRepository.findById(flightId).orElseThrow(()->new ResourceNotFoundException("Flight","id",flightId));
        Passenger passenger = passengerRepository.findById(passengerId).orElseThrow(() -> new ResourceNotFoundException("Passenger","id", passengerId));
        Integer seatNumber = getNextSeatNumber(flightId);
        if (seatNumber<=0)
            throw new RuntimeException("Flight is full for reservation, please try again later");
        reservation.setPassenger(passenger);
        reservation.setFlight(flight);
        reservation.setStatus("R");
        reservation.setSeatNumber(seatNumber);
        reservation.setPnr(generatePnr());
        flight.setSeatsLeft(flight.getSeatsLeft()-1);
        flightRepository.save(flight);
        reservationRepository.save(reservation);
        reservationRepository.deleteCancelledReservations(reservation.getSeatNumber(),reservation.getFlight().getId());
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setPnr(reservation.getPnr());
        reservationDto.setStatus(reservation.getStatus());
        reservationDto.setSeatNumber(reservation.getSeatNumber());
        return reservationDto;
    }

    @Override
    public ReservationDto cancelReservation(String pnr) {
        Reservation reservation = reservationRepository.findByPnr(pnr).orElseThrow(()->new ResourceNotFoundException("Reservation","PNR",pnr));
        if (!reservation.getStatus().equals("R"))
            throw new RuntimeException("Please check the reservation status");
        Flight flight = reservation.getFlight();
        flightRepository.save(flight);
        reservation.setStatus("C");
        reservationRepository.save(reservation);
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setSeatNumber(reservation.getSeatNumber());
        reservationDto.setPnr(reservation.getPnr());
        reservationDto.setStatus(reservation.getStatus());
        return reservationDto;
    }

    @Override
    public ReservationDto updateReservationStatus(String status, long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(()->new ResourceNotFoundException("Reservation","id",reservationId));
        reservation.setStatus(status);
        reservationRepository.save(reservation);
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setStatus(reservation.getStatus());
        reservationDto.setSeatNumber(reservation.getSeatNumber());
        reservationDto.setPnr(reservation.getPnr());
        return reservationDto;
    }

    private int getNextSeatNumber(Long flightId){
        Integer seatNumber = reservationRepository.retrieveNextSeatNumber(flightId);

        seatNumber = seatNumber==null?0:seatNumber;

        if (seatNumber>0)
            return seatNumber;
        else {
            seatNumber = reservationRepository.retrieveAnyCancelledSeatNumber(flightId);
            seatNumber = seatNumber==null?0:seatNumber;
            return seatNumber;
        }
    }

    private String generatePnr() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder sb = new StringBuilder();
        Random rnd = new Random();
        while (sb.length() < 7) {
            int index = (int) (rnd.nextFloat() * chars.length());
            sb.append(chars.charAt(index));
        }
        String pnr = sb.toString();
        return pnr;
    }

}
