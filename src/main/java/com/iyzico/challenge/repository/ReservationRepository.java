package com.iyzico.challenge.repository;

import com.iyzico.challenge.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {


    public Optional<Reservation> findByPnr(String pnr);

    @Query("SELECT min(r.seatNumber) " +
            "FROM Reservation r, Flight f " +
            "WHERE f.id = (:flightId) " +
            "AND r.flight = f " +
            "AND r.status = 'C'")
    public Integer retrieveAnyCancelledSeatNumber(@Param("flightId") Long flightId);

    @Query("SELECT f.maxSeats-f.seatsLeft+1 " +
            "FROM Flight f " +
            "WHERE f.id = (:flightId) " +
            "AND seatsLeft>0")
    public Integer retrieveNextSeatNumber(@Param("flightId") Long flightId);

    @Modifying
    @Query(value = "UPDATE Reservations r SET r.status = 'D' WHERE r.flight_id = :flightId AND r.status = 'C' AND seat_number = :seatNumber"
    ,nativeQuery = true)
    public void deleteCancelledReservations(@Param("seatNumber") int seatNumber, @Param("flightId") Long flightId);



}
