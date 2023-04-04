package com.iyzico.challenge.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.iyzico.challenge.dto.ReservationDto;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Passenger;
import com.iyzico.challenge.entity.Reservation;
import com.iyzico.challenge.exception.ResourceNotFoundException;
import com.iyzico.challenge.repository.FlightRepository;
import com.iyzico.challenge.repository.PassengerRepository;
import com.iyzico.challenge.repository.ReservationRepository;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ReservationServiceImpl.class})
@ExtendWith(SpringExtension.class)
class ReservationServiceImplTest {
    @MockBean
    private FlightRepository flightRepository;

    @MockBean
    private PassengerRepository passengerRepository;

    @MockBean
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationServiceImpl reservationServiceImpl;
    
    @Test
    void testGetReservation() {
        Flight flight = new Flight();
        flight.setArrival("Amsterdam");
        flight.setDeparture("Istanbul");
        flight.setFlightNumber("42");
        flight.setId(1L);
        flight.setMaxSeats(3);
        flight.setSeatPrice(BigDecimal.valueOf(42L));
        flight.setSeatsLeft(1);

        Passenger passenger = new Passenger();
        passenger.setAge(1);
        passenger.setEmail("gunduc@gmail.com");
        passenger.setId(1L);
        passenger.setName("Cengiz");
        passenger.setSurname("Gunduc");

        Reservation reservation = new Reservation();
        reservation.setFlight(flight);
        reservation.setId(1L);
        reservation.setPassenger(passenger);
        reservation.setPaymentId(123L);
        reservation.setPnr("Pnr");
        reservation.setSeatNumber(10);
        reservation.setStatus("Status");
        reservation.setVersion(1);
        Optional<Reservation> ofResult = Optional.of(reservation);
        when(reservationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        Reservation actualReservation = reservationServiceImpl.getReservation(1L);
        assertSame(reservation, actualReservation);
        assertEquals("42", actualReservation.getFlight().getSeatPrice().toString());
        verify(reservationRepository).findById(Mockito.<Long>any());
    }


    @Test
    void testGetReservation2() {
        when(reservationRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> reservationServiceImpl.getReservation(1L));
        verify(reservationRepository).findById(Mockito.<Long>any());
    }


    @Test
    void testGetReservation3() {
        when(reservationRepository.findById(Mockito.<Long>any())).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> reservationServiceImpl.getReservation(1L));
        verify(reservationRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testGetFlightFromReservation() {
        Flight flight = new Flight();
        flight.setArrival("Amsterdam");
        flight.setDeparture("Istanbul");
        flight.setFlightNumber("42");
        flight.setId(1L);
        flight.setMaxSeats(3);
        flight.setSeatPrice(BigDecimal.valueOf(42L));
        flight.setSeatsLeft(1);

        Passenger passenger = new Passenger();
        passenger.setAge(1);
        passenger.setEmail("gunduc@gmail.com");
        passenger.setId(1L);
        passenger.setName("Cengiz");
        passenger.setSurname("Gunduc");

        Reservation reservation = new Reservation();
        reservation.setFlight(flight);
        reservation.setId(1L);
        reservation.setPassenger(passenger);
        reservation.setPaymentId(123L);
        reservation.setPnr("Pnr");
        reservation.setSeatNumber(10);
        reservation.setStatus("Status");
        reservation.setVersion(1);
        Optional<Reservation> ofResult = Optional.of(reservation);
        when(reservationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        Flight actualFlightFromReservation = reservationServiceImpl.getFlightFromReservation(1L);
        assertSame(flight, actualFlightFromReservation);
        assertEquals("42", actualFlightFromReservation.getSeatPrice().toString());
        verify(reservationRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testGetFlightFromReservation2() {
        when(reservationRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> reservationServiceImpl.getFlightFromReservation(1L));
        verify(reservationRepository).findById(Mockito.<Long>any());
    }


    @Test
    void testGetFlightFromReservation3() {
        when(reservationRepository.findById(Mockito.<Long>any())).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> reservationServiceImpl.getFlightFromReservation(1L));
        verify(reservationRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testMakeReservation() {
        Flight flight = new Flight();
        flight.setArrival("Amsterdam");
        flight.setDeparture("Istanbul");
        flight.setFlightNumber("42");
        flight.setId(1L);
        flight.setMaxSeats(3);
        flight.setSeatPrice(BigDecimal.valueOf(42L));
        flight.setSeatsLeft(1);

        Passenger passenger = new Passenger();
        passenger.setAge(1);
        passenger.setEmail("gunduc@gmail.com");
        passenger.setId(1L);
        passenger.setName("Cengiz");
        passenger.setSurname("Gunduc");

        Reservation reservation = new Reservation();
        reservation.setFlight(flight);
        reservation.setId(1L);
        reservation.setPassenger(passenger);
        reservation.setPaymentId(123L);
        reservation.setPnr("Pnr");
        reservation.setSeatNumber(10);
        reservation.setStatus("Status");
        reservation.setVersion(1);
        when(reservationRepository.save(Mockito.<Reservation>any())).thenReturn(reservation);
        doNothing().when(reservationRepository).deleteCancelledReservations(anyInt(), Mockito.<Long>any());
        when(reservationRepository.retrieveAnyCancelledSeatNumber(Mockito.<Long>any())).thenReturn(10);
        when(reservationRepository.retrieveNextSeatNumber(Mockito.<Long>any())).thenReturn(10);

        Flight flight2 = new Flight();
        flight2.setArrival("Amsterdam");
        flight2.setDeparture("Istanbul");
        flight2.setFlightNumber("42");
        flight2.setId(1L);
        flight2.setMaxSeats(3);
        flight2.setSeatPrice(BigDecimal.valueOf(42L));
        flight2.setSeatsLeft(1);
        Optional<Flight> ofResult = Optional.of(flight2);

        Flight flight3 = new Flight();
        flight3.setArrival("Amsterdam");
        flight3.setDeparture("Istanbul");
        flight3.setFlightNumber("42");
        flight3.setId(1L);
        flight3.setMaxSeats(3);
        flight3.setSeatPrice(BigDecimal.valueOf(42L));
        flight3.setSeatsLeft(1);
        when(flightRepository.save(Mockito.<Flight>any())).thenReturn(flight3);
        when(flightRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Passenger passenger2 = new Passenger();
        passenger2.setAge(1);
        passenger2.setEmail("gunduc@gmail.com");
        passenger2.setId(1L);
        passenger2.setName("Cengiz");
        passenger2.setSurname("Gunduc");
        Optional<Passenger> ofResult2 = Optional.of(passenger2);
        when(passengerRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        ReservationDto actualMakeReservationResult = reservationServiceImpl.makeReservation(123L, 123L);
        assertEquals("R", actualMakeReservationResult.getStatus());
        assertEquals(10, actualMakeReservationResult.getSeatNumber());
        verify(reservationRepository).retrieveNextSeatNumber(Mockito.<Long>any());
        verify(reservationRepository).save(Mockito.<Reservation>any());
        verify(reservationRepository).deleteCancelledReservations(anyInt(), Mockito.<Long>any());
        verify(flightRepository).save(Mockito.<Flight>any());
        verify(flightRepository).findById(Mockito.<Long>any());
        verify(passengerRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testMakeReservation2() {
        Flight flight = new Flight();
        flight.setArrival("Amsterdam");
        flight.setDeparture("Istanbul");
        flight.setFlightNumber("42");
        flight.setId(1L);
        flight.setMaxSeats(3);
        flight.setSeatPrice(BigDecimal.valueOf(42L));
        flight.setSeatsLeft(1);

        Passenger passenger = new Passenger();
        passenger.setAge(1);
        passenger.setEmail("gunduc@gmail.com");
        passenger.setId(1L);
        passenger.setName("Cengiz");
        passenger.setSurname("Gunduc");

        Reservation reservation = new Reservation();
        reservation.setFlight(flight);
        reservation.setId(1L);
        reservation.setPassenger(passenger);
        reservation.setPaymentId(123L);
        reservation.setPnr("Pnr");
        reservation.setSeatNumber(10);
        reservation.setStatus("Status");
        reservation.setVersion(1);
        when(reservationRepository.save(Mockito.<Reservation>any())).thenReturn(reservation);
        doNothing().when(reservationRepository).deleteCancelledReservations(anyInt(), Mockito.<Long>any());
        when(reservationRepository.retrieveAnyCancelledSeatNumber(Mockito.<Long>any())).thenReturn(10);
        when(reservationRepository.retrieveNextSeatNumber(Mockito.<Long>any())).thenReturn(10);

        Flight flight2 = new Flight();
        flight2.setArrival("Amsterdam");
        flight2.setDeparture("Istanbul");
        flight2.setFlightNumber("42");
        flight2.setId(1L);
        flight2.setMaxSeats(3);
        flight2.setSeatPrice(BigDecimal.valueOf(42L));
        flight2.setSeatsLeft(1);
        Optional<Flight> ofResult = Optional.of(flight2);
        when(flightRepository.save(Mockito.<Flight>any())).thenThrow(new RuntimeException());
        when(flightRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Passenger passenger2 = new Passenger();
        passenger2.setAge(1);
        passenger2.setEmail("gunduc@gmail.com");
        passenger2.setId(1L);
        passenger2.setName("Cengiz");
        passenger2.setSurname("Gunduc");
        Optional<Passenger> ofResult2 = Optional.of(passenger2);
        when(passengerRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        assertThrows(RuntimeException.class, () -> reservationServiceImpl.makeReservation(123L, 123L));
        verify(reservationRepository).retrieveNextSeatNumber(Mockito.<Long>any());
        verify(flightRepository).save(Mockito.<Flight>any());
        verify(flightRepository).findById(Mockito.<Long>any());
        verify(passengerRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testMakeReservation3() {
        Flight flight = new Flight();
        flight.setArrival("Amsterdam");
        flight.setDeparture("Istanbul");
        flight.setFlightNumber("42");
        flight.setId(1L);
        flight.setMaxSeats(3);
        flight.setSeatPrice(BigDecimal.valueOf(42L));
        flight.setSeatsLeft(1);

        Passenger passenger = new Passenger();
        passenger.setAge(1);
        passenger.setEmail("gunduc@gmail.com");
        passenger.setId(1L);
        passenger.setName("Cengiz");
        passenger.setSurname("Gunduc");

        Reservation reservation = new Reservation();
        reservation.setFlight(flight);
        reservation.setId(1L);
        reservation.setPassenger(passenger);
        reservation.setPaymentId(123L);
        reservation.setPnr("Pnr");
        reservation.setSeatNumber(10);
        reservation.setStatus("Status");
        reservation.setVersion(1);
        when(reservationRepository.save(Mockito.<Reservation>any())).thenReturn(reservation);
        doNothing().when(reservationRepository).deleteCancelledReservations(anyInt(), Mockito.<Long>any());
        when(reservationRepository.retrieveAnyCancelledSeatNumber(Mockito.<Long>any())).thenReturn(10);
        when(reservationRepository.retrieveNextSeatNumber(Mockito.<Long>any())).thenReturn(0);

        Flight flight2 = new Flight();
        flight2.setArrival("Amsterdam");
        flight2.setDeparture("Istanbul");
        flight2.setFlightNumber("42");
        flight2.setId(1L);
        flight2.setMaxSeats(3);
        flight2.setSeatPrice(BigDecimal.valueOf(42L));
        flight2.setSeatsLeft(1);
        Optional<Flight> ofResult = Optional.of(flight2);

        Flight flight3 = new Flight();
        flight3.setArrival("Amsterdam");
        flight3.setDeparture("Istanbul");
        flight3.setFlightNumber("42");
        flight3.setId(1L);
        flight3.setMaxSeats(3);
        flight3.setSeatPrice(BigDecimal.valueOf(42L));
        flight3.setSeatsLeft(1);
        when(flightRepository.save(Mockito.<Flight>any())).thenReturn(flight3);
        when(flightRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Passenger passenger2 = new Passenger();
        passenger2.setAge(1);
        passenger2.setEmail("gunduc@gmail.com");
        passenger2.setId(1L);
        passenger2.setName("Cengiz");
        passenger2.setSurname("Gunduc");
        Optional<Passenger> ofResult2 = Optional.of(passenger2);
        when(passengerRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        ReservationDto actualMakeReservationResult = reservationServiceImpl.makeReservation(123L, 123L);
        assertEquals("R", actualMakeReservationResult.getStatus());
        assertEquals(10, actualMakeReservationResult.getSeatNumber());
        verify(reservationRepository).retrieveAnyCancelledSeatNumber(Mockito.<Long>any());
        verify(reservationRepository).retrieveNextSeatNumber(Mockito.<Long>any());
        verify(reservationRepository).save(Mockito.<Reservation>any());
        verify(reservationRepository).deleteCancelledReservations(anyInt(), Mockito.<Long>any());
        verify(flightRepository).save(Mockito.<Flight>any());
        verify(flightRepository).findById(Mockito.<Long>any());
        verify(passengerRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testMakeReservation4() {
        Flight flight = new Flight();
        flight.setArrival("Amsterdam");
        flight.setDeparture("Istanbul");
        flight.setFlightNumber("42");
        flight.setId(1L);
        flight.setMaxSeats(3);
        flight.setSeatPrice(BigDecimal.valueOf(42L));
        flight.setSeatsLeft(1);

        Passenger passenger = new Passenger();
        passenger.setAge(1);
        passenger.setEmail("gunduc@gmail.com");
        passenger.setId(1L);
        passenger.setName("Cengiz");
        passenger.setSurname("Gunduc");

        Reservation reservation = new Reservation();
        reservation.setFlight(flight);
        reservation.setId(1L);
        reservation.setPassenger(passenger);
        reservation.setPaymentId(123L);
        reservation.setPnr("Pnr");
        reservation.setSeatNumber(10);
        reservation.setStatus("Status");
        reservation.setVersion(1);
        when(reservationRepository.save(Mockito.<Reservation>any())).thenReturn(reservation);
        doNothing().when(reservationRepository).deleteCancelledReservations(anyInt(), Mockito.<Long>any());
        when(reservationRepository.retrieveAnyCancelledSeatNumber(Mockito.<Long>any())).thenReturn(10);
        when(reservationRepository.retrieveNextSeatNumber(Mockito.<Long>any())).thenReturn(null);

        Flight flight2 = new Flight();
        flight2.setArrival("Amsterdam");
        flight2.setDeparture("Istanbul");
        flight2.setFlightNumber("42");
        flight2.setId(1L);
        flight2.setMaxSeats(3);
        flight2.setSeatPrice(BigDecimal.valueOf(42L));
        flight2.setSeatsLeft(1);
        Optional<Flight> ofResult = Optional.of(flight2);

        Flight flight3 = new Flight();
        flight3.setArrival("Amsterdam");
        flight3.setDeparture("Istanbul");
        flight3.setFlightNumber("42");
        flight3.setId(1L);
        flight3.setMaxSeats(3);
        flight3.setSeatPrice(BigDecimal.valueOf(42L));
        flight3.setSeatsLeft(1);
        when(flightRepository.save(Mockito.<Flight>any())).thenReturn(flight3);
        when(flightRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Passenger passenger2 = new Passenger();
        passenger2.setAge(1);
        passenger2.setEmail("gunduc@gmail.com");
        passenger2.setId(1L);
        passenger2.setName("Cengiz");
        passenger2.setSurname("Gunduc");
        Optional<Passenger> ofResult2 = Optional.of(passenger2);
        when(passengerRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);
        ReservationDto actualMakeReservationResult = reservationServiceImpl.makeReservation(123L, 123L);
        assertEquals("R", actualMakeReservationResult.getStatus());
        assertEquals(10, actualMakeReservationResult.getSeatNumber());
        verify(reservationRepository).retrieveAnyCancelledSeatNumber(Mockito.<Long>any());
        verify(reservationRepository).retrieveNextSeatNumber(Mockito.<Long>any());
        verify(reservationRepository).save(Mockito.<Reservation>any());
        verify(reservationRepository).deleteCancelledReservations(anyInt(), Mockito.<Long>any());
        verify(flightRepository).save(Mockito.<Flight>any());
        verify(flightRepository).findById(Mockito.<Long>any());
        verify(passengerRepository).findById(Mockito.<Long>any());
    }

    @Test
    void testCancelReservation() {
        Flight flight = new Flight();
        flight.setArrival("Amsterdam");
        flight.setDeparture("Istanbul");
        flight.setFlightNumber("42");
        flight.setId(1L);
        flight.setMaxSeats(3);
        flight.setSeatPrice(BigDecimal.valueOf(42L));
        flight.setSeatsLeft(1);

        Passenger passenger = new Passenger();
        passenger.setAge(1);
        passenger.setEmail("gunduc@gmail.com");
        passenger.setId(1L);
        passenger.setName("Cengiz");
        passenger.setSurname("Gunduc");

        Reservation reservation = new Reservation();
        reservation.setFlight(flight);
        reservation.setId(1L);
        reservation.setPassenger(passenger);
        reservation.setPaymentId(123L);
        reservation.setPnr("Pnr");
        reservation.setSeatNumber(10);
        reservation.setStatus("Status");
        reservation.setVersion(1);
        Optional<Reservation> ofResult = Optional.of(reservation);
        when(reservationRepository.findByPnr(Mockito.<String>any())).thenReturn(ofResult);
        assertThrows(RuntimeException.class, () -> reservationServiceImpl.cancelReservation("Pnr"));
        verify(reservationRepository).findByPnr(Mockito.<String>any());
    }

    @Test
    void testCancelReservation2() {
        when(reservationRepository.findByPnr(Mockito.<String>any())).thenReturn(Optional.empty());

        Flight flight = new Flight();
        flight.setArrival("Amsterdam");
        flight.setDeparture("Istanbul");
        flight.setFlightNumber("42");
        flight.setId(1L);
        flight.setMaxSeats(3);
        flight.setSeatPrice(BigDecimal.valueOf(42L));
        flight.setSeatsLeft(1);

        Passenger passenger = new Passenger();
        passenger.setAge(1);
        passenger.setEmail("gunduc@gmail.com");
        passenger.setId(1L);
        passenger.setName("Cengiz");
        passenger.setSurname("Gunduc");
        Reservation reservation = mock(Reservation.class);
        when(reservation.getStatus()).thenReturn("R");
        doNothing().when(reservation).setFlight(Mockito.<Flight>any());
        doNothing().when(reservation).setId(Mockito.<Long>any());
        doNothing().when(reservation).setPassenger(Mockito.<Passenger>any());
        doNothing().when(reservation).setPaymentId(Mockito.<Long>any());
        doNothing().when(reservation).setPnr(Mockito.<String>any());
        doNothing().when(reservation).setSeatNumber(anyInt());
        doNothing().when(reservation).setStatus(Mockito.<String>any());
        doNothing().when(reservation).setVersion(Mockito.<Integer>any());
        reservation.setFlight(flight);
        reservation.setId(1L);
        reservation.setPassenger(passenger);
        reservation.setPaymentId(123L);
        reservation.setPnr("Pnr");
        reservation.setSeatNumber(10);
        reservation.setStatus("R");
        reservation.setVersion(1);
        assertThrows(ResourceNotFoundException.class, () -> reservationServiceImpl.cancelReservation("Pnr"));
        verify(reservationRepository).findByPnr(Mockito.<String>any());
        verify(reservation).setFlight(Mockito.<Flight>any());
        verify(reservation).setId(Mockito.<Long>any());
        verify(reservation).setPassenger(Mockito.<Passenger>any());
        verify(reservation).setPaymentId(Mockito.<Long>any());
        verify(reservation).setPnr(Mockito.<String>any());
        verify(reservation).setSeatNumber(anyInt());
        verify(reservation).setStatus(Mockito.<String>any());
        verify(reservation).setVersion(Mockito.<Integer>any());
    }

    @Test
    void testUpdateReservationStatus() {
        Flight flight = new Flight();
        flight.setArrival("Amsterdam");
        flight.setDeparture("Istanbul");
        flight.setFlightNumber("42");
        flight.setId(1L);
        flight.setMaxSeats(3);
        flight.setSeatPrice(BigDecimal.valueOf(42L));
        flight.setSeatsLeft(1);

        Passenger passenger = new Passenger();
        passenger.setAge(1);
        passenger.setEmail("gunduc@gmail.com");
        passenger.setId(1L);
        passenger.setName("Cengiz");
        passenger.setSurname("Gunduc");

        Reservation reservation = new Reservation();
        reservation.setFlight(flight);
        reservation.setId(1L);
        reservation.setPassenger(passenger);
        reservation.setPaymentId(123L);
        reservation.setPnr("Pnr");
        reservation.setSeatNumber(10);
        reservation.setStatus("R");
        reservation.setVersion(1);
        Optional<Reservation> ofResult = Optional.of(reservation);

        Flight flight2 = new Flight();
        flight2.setArrival("Amsterdam");
        flight2.setDeparture("Istanbul");
        flight2.setFlightNumber("42");
        flight2.setId(1L);
        flight2.setMaxSeats(3);
        flight2.setSeatPrice(BigDecimal.valueOf(42L));
        flight2.setSeatsLeft(1);

        Passenger passenger2 = new Passenger();
        passenger2.setAge(1);
        passenger2.setEmail("gunduc@gmail.com");
        passenger2.setId(1L);
        passenger2.setName("Cengiz");
        passenger2.setSurname("Gunduc");

        Reservation reservation2 = new Reservation();
        reservation2.setFlight(flight2);
        reservation2.setId(1L);
        reservation2.setPassenger(passenger2);
        reservation2.setPaymentId(123L);
        reservation2.setPnr("Pnr");
        reservation2.setSeatNumber(10);
        reservation2.setStatus("R");
        reservation2.setVersion(1);
        when(reservationRepository.save(Mockito.<Reservation>any())).thenReturn(reservation2);
        when(reservationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        ReservationDto actualUpdateReservationStatusResult = reservationServiceImpl.updateReservationStatus("R",
                123L);
        assertEquals("Pnr", actualUpdateReservationStatusResult.getPnr());
        assertEquals("R", actualUpdateReservationStatusResult.getStatus());
        assertEquals(10, actualUpdateReservationStatusResult.getSeatNumber());
        verify(reservationRepository).save(Mockito.<Reservation>any());
        verify(reservationRepository).findById(Mockito.<Long>any());
    }


    @Test
    void testUpdateReservationStatus2() {
        Flight flight = new Flight();
        flight.setArrival("Amsterdam");
        flight.setDeparture("Istanbul");
        flight.setFlightNumber("42");
        flight.setId(1L);
        flight.setMaxSeats(3);
        flight.setSeatPrice(BigDecimal.valueOf(42L));
        flight.setSeatsLeft(1);

        Passenger passenger = new Passenger();
        passenger.setAge(1);
        passenger.setEmail("gunduc@gmail.com");
        passenger.setId(1L);
        passenger.setName("Cengiz");
        passenger.setSurname("Gunduc");

        Reservation reservation = new Reservation();
        reservation.setFlight(flight);
        reservation.setId(1L);
        reservation.setPassenger(passenger);
        reservation.setPaymentId(123L);
        reservation.setPnr("Pnr");
        reservation.setSeatNumber(10);
        reservation.setStatus("R");
        reservation.setVersion(1);
        Optional<Reservation> ofResult = Optional.of(reservation);
        when(reservationRepository.save(Mockito.<Reservation>any())).thenThrow(new RuntimeException());
        when(reservationRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(RuntimeException.class, () -> reservationServiceImpl.updateReservationStatus("R", 123L));
        verify(reservationRepository).save(Mockito.<Reservation>any());
        verify(reservationRepository).findById(Mockito.<Long>any());
    }
}

