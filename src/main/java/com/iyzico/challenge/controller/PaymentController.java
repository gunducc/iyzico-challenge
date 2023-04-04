package com.iyzico.challenge.controller;

import com.iyzico.challenge.dto.PaymentDto;
import com.iyzico.challenge.dto.ReservationDto;
import com.iyzico.challenge.entity.Payment;
import com.iyzico.challenge.entity.Reservation;
import com.iyzico.challenge.service.IyzicoPaymentService;
import com.iyzico.challenge.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequestMapping("api/payment")
public class PaymentController {
    private IyzicoPaymentService iyzicoPaymentService;

    private ReservationService reservationService;

    public PaymentController(IyzicoPaymentService iyzicoPaymentService, ReservationService reservationService){
        this.iyzicoPaymentService = iyzicoPaymentService;
        this.reservationService = reservationService;
    }

    @PostMapping("/reservation/{reservationId}")
    public ResponseEntity<ReservationDto> buyTicket(@PathVariable(name = "reservationId") long reservationId, @RequestBody @Valid PaymentDto paymentDto){
        BigDecimal latestPrice = reservationService.getFlightFromReservation(reservationId).getSeatPrice();

        if (!paymentDto.getPrice().equals(latestPrice)){
            Exception e = new RuntimeException();
            throw new RuntimeException("Payment amount doesn't match with the latest seat price, please check the latest flight information",e.getCause());
        }
        Reservation reservation = reservationService.getReservation(reservationId);
        if (!reservation.getStatus().equals("R"))
            throw new RuntimeException("Please check the reservation status, it is already paid or cancelled");

        iyzicoPaymentService.pay(paymentDto.getPrice());
        return new ResponseEntity<ReservationDto>(reservationService.updateReservationStatus("P",reservationId),HttpStatus.OK);

    }


}
