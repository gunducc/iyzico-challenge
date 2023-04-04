package com.iyzico.challenge.controller;

import com.iyzico.challenge.dto.ReservationDto;
import com.iyzico.challenge.service.IyzicoPaymentService;
import com.iyzico.challenge.service.ReservationService;
import org.hibernate.annotations.OptimisticLock;
import org.hibernate.annotations.OptimisticLocking;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/reservation")
public class ReservationController {

    private ReservationService reservationService;

    public ReservationController(ReservationService reservationService, IyzicoPaymentService paymentService){
        this.reservationService = reservationService;
    }

    @PostMapping("flight/{flightId}/passenger/{passengerId}")
    public ResponseEntity<ReservationDto> makeReservation (@PathVariable (name = "flightId") long flightId,
                                                    @PathVariable (name = "passengerId") long passengerId){
        return new ResponseEntity<ReservationDto>(reservationService.makeReservation(passengerId, flightId), HttpStatus.OK);
    }

    @PostMapping("cancel/{pnr}")
    public ResponseEntity<ReservationDto> cancelReservation (@PathVariable (name = "pnr") String pnr){
        return new ResponseEntity<ReservationDto>(reservationService.cancelReservation(pnr),HttpStatus.OK);
    }


}
