package com.iyzico.challenge.controller;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iyzico.challenge.dto.PaymentDto;
import com.iyzico.challenge.dto.ReservationDto;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Passenger;
import com.iyzico.challenge.entity.Reservation;
import com.iyzico.challenge.service.IyzicoPaymentService;
import com.iyzico.challenge.service.ReservationService;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {PaymentController.class})
@ExtendWith(SpringExtension.class)
class PaymentControllerTest {
    @MockBean
    private IyzicoPaymentService iyzicoPaymentService;

    @Autowired
    private PaymentController paymentController;

    @MockBean
    private ReservationService reservationService;

    @Test
    void testBuyTicket() throws Exception {
        when(iyzicoPaymentService.pay(Mockito.<BigDecimal>any())).thenReturn(true);

        Flight flight = new Flight();
        flight.setArrival("Amsterdam");
        flight.setDeparture("Istanbul");
        flight.setFlightNumber("42");
        flight.setId(1L);
        flight.setMaxSeats(3);
        flight.setSeatPrice(BigDecimal.valueOf(42L));
        flight.setSeatsLeft(1);

        Flight flight2 = new Flight();
        flight2.setArrival("Istanbul");
        flight2.setDeparture("Berlin");
        flight2.setFlightNumber("42");
        flight2.setId(1L);
        flight2.setMaxSeats(3);
        flight2.setSeatPrice(BigDecimal.valueOf(42L));
        flight2.setSeatsLeft(1);

        Passenger passenger = new Passenger();
        passenger.setAge(1);
        passenger.setEmail("gunduc@gmail.com");
        passenger.setId(1L);
        passenger.setName("Cengiz");
        passenger.setSurname("Gunduc");

        Reservation reservation = new Reservation();
        reservation.setFlight(flight2);
        reservation.setId(1L);
        reservation.setPassenger(passenger);
        reservation.setPaymentId(123L);
        reservation.setPnr("Pnr");
        reservation.setSeatNumber(10);
        reservation.setStatus("R");
        reservation.setVersion(1);

        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setPnr("Pnr");
        reservationDto.setSeatNumber(10);
        reservationDto.setStatus("R");
        when(reservationService.updateReservationStatus(Mockito.<String>any(), anyLong())).thenReturn(reservationDto);
        when(reservationService.getReservation(anyLong())).thenReturn(reservation);
        when(reservationService.getFlightFromReservation(anyLong())).thenReturn(flight);

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setPrice(BigDecimal.valueOf(42L));
        String content = (new ObjectMapper()).writeValueAsString(paymentDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/payment/reservation/{reservationId}", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(paymentController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"pnr\":\"Pnr\",\"seatNumber\":10,\"status\":\"R\"}"));
    }
}

