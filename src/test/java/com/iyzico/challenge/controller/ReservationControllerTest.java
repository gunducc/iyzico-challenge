package com.iyzico.challenge.controller;

import static org.mockito.Mockito.when;

import com.iyzico.challenge.dto.ReservationDto;
import com.iyzico.challenge.service.IyzicoPaymentService;
import com.iyzico.challenge.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {ReservationController.class})
@ExtendWith(SpringExtension.class)
class ReservationControllerTest {
    @MockBean
    private IyzicoPaymentService iyzicoPaymentService;

    @Autowired
    private ReservationController reservationController;

    @MockBean
    private ReservationService reservationService;


    @Test
    void makeReservation() throws Exception {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setSeatNumber(10);
        reservationDto.setStatus("R");
        when(reservationService.makeReservation(Mockito.<Long>any(), Mockito.<Long>any())).thenReturn(reservationDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/reservation/flight/{flightId}/passenger/{passengerId}", 1, 1);
        MockMvcBuilders.standaloneSetup(reservationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    @Test
    void cancelReservation() throws Exception {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setPnr("Pnr");
        reservationDto.setSeatNumber(10);
        reservationDto.setStatus("R");
        when(reservationService.cancelReservation(Mockito.<String>any())).thenReturn(reservationDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/reservation/cancel/{pnr}", "Pnr");
        MockMvcBuilders.standaloneSetup(reservationController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }
}

