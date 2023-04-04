package com.iyzico.challenge.controller;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iyzico.challenge.dto.FlightDto;
import com.iyzico.challenge.dto.PaginatedResponse;
import com.iyzico.challenge.service.FlightService;

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

@ContextConfiguration(classes = {FlightController.class})
@ExtendWith(SpringExtension.class)
class FlightControllerTest {
    @Autowired
    private FlightController flightController;

    @MockBean
    private FlightService flightService;


    @Test
    void testGetAllFlights() throws Exception {
        when(flightService.getAllFlights(anyInt(), anyInt(), Mockito.<String>any()))
                .thenReturn(new PaginatedResponse<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/flight");
        MockMvcBuilders.standaloneSetup(flightController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"content\":null,\"pageNo\":0,\"pageSize\":0,\"totalElements\":0,\"totalPages\":0,\"last\":false}"));
    }


    @Test
    void testCreateFlight() throws Exception {
        when(flightService.getAllFlights(anyInt(), anyInt(), Mockito.<String>any())).thenReturn(new PaginatedResponse<>());

        FlightDto flightDto = new FlightDto();
        flightDto.setArrival("Arrival");
        flightDto.setDeparture("Departure");
        flightDto.setFlightNumber("42");
        flightDto.setId(1L);
        flightDto.setMaxSeats(3);
        flightDto.setSeatPrice(BigDecimal.valueOf(42L));
        flightDto.setSeatsLeft(1);
        String content = (new ObjectMapper()).writeValueAsString(flightDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/flight")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(flightController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"content\":null,\"pageNo\":0,\"pageSize\":0,\"totalElements\":0,\"totalPages\":0,\"last\":false}"));
    }


    @Test
    void testFindFlightById() throws Exception {
        FlightDto flightDto = new FlightDto();
        flightDto.setArrival("Amsterdam");
        flightDto.setDeparture("Istanbul");
        flightDto.setFlightNumber("42");
        flightDto.setId(1L);
        flightDto.setMaxSeats(3);
        flightDto.setSeatPrice(BigDecimal.valueOf(42L));
        flightDto.setSeatsLeft(1);
        when(flightService.findFlightById(anyLong())).thenReturn(flightDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/flight/{id}", 1L);
        MockMvcBuilders.standaloneSetup(flightController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"flightNumber\":\"42\",\"departure\":\"Istanbul\",\"arrival\":\"Amsterdam\",\"seatsLeft\":1,\"maxSeats\":3,"
                                        + "\"seatPrice\":42}"));
    }


    @Test
    void testUpdateFlight() throws Exception {
        FlightDto flightDto = new FlightDto();
        flightDto.setArrival("Amsterdam");
        flightDto.setDeparture("Istanbul");
        flightDto.setFlightNumber("42");
        flightDto.setId(1L);
        flightDto.setMaxSeats(3);
        flightDto.setSeatPrice(BigDecimal.valueOf(42L));
        flightDto.setSeatsLeft(1);
        when(flightService.updateFlight(Mockito.<FlightDto>any(), anyLong())).thenReturn(flightDto);

        FlightDto flightDto2 = new FlightDto();
        flightDto2.setArrival("Amsterdam");
        flightDto2.setDeparture("Istanbul");
        flightDto2.setFlightNumber("42");
        flightDto2.setId(1L);
        flightDto2.setMaxSeats(3);
        flightDto2.setSeatPrice(BigDecimal.valueOf(42L));
        flightDto2.setSeatsLeft(1);
        String content = (new ObjectMapper()).writeValueAsString(flightDto2);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/flight/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(flightController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":1,\"flightNumber\":\"42\",\"departure\":\"Istanbul\",\"arrival\":\"Amsterdam\",\"seatsLeft\":1,\"maxSeats\":3,"
                                        + "\"seatPrice\":42}"));
    }
}

