package com.iyzico.challenge.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.iyzico.challenge.dto.FlightDto;
import com.iyzico.challenge.dto.PaginatedResponse;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.exception.ResourceNotFoundException;
import com.iyzico.challenge.repository.FlightRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {FlightServiceImpl.class})
@ExtendWith(SpringExtension.class)
class FlightServiceImplTest {
    @MockBean
    private FlightRepository flightRepository;

    @Autowired
    private FlightServiceImpl flightServiceImpl;

    @MockBean
    private ModelMapper modelMapper;


    @Test
    void testCreateFlight() {
        Flight flight = new Flight();
        flight.setArrival("Amsterdam");
        flight.setDeparture("Istanbul");
        flight.setFlightNumber("42");
        flight.setId(1L);
        flight.setMaxSeats(3);
        flight.setSeatPrice(BigDecimal.valueOf(42L));
        flight.setSeatsLeft(1);
        when(flightRepository.save(Mockito.<Flight>any())).thenReturn(flight);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<Object>>any()))
                .thenThrow(new ResourceNotFoundException("Flight", "id", 42L));

        FlightDto flightDto = new FlightDto();
        flightDto.setArrival("Amsterdam");
        flightDto.setDeparture("Istanbul");
        flightDto.setFlightNumber("42");
        flightDto.setId(1L);
        flightDto.setMaxSeats(3);
        flightDto.setSeatPrice(BigDecimal.valueOf(42L));
        flightDto.setSeatsLeft(1);
        assertThrows(ResourceNotFoundException.class, () -> flightServiceImpl.createFlight(flightDto));
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<Flight>>any());
    }


    @Test
    void testCreateFlight2() {
        Flight flight = mock(Flight.class);
        doNothing().when(flight).setArrival(Mockito.<String>any());
        doNothing().when(flight).setDeparture(Mockito.<String>any());
        doNothing().when(flight).setFlightNumber(Mockito.<String>any());
        doNothing().when(flight).setId(Mockito.<Long>any());
        doNothing().when(flight).setMaxSeats(anyInt());
        doNothing().when(flight).setSeatPrice(Mockito.<BigDecimal>any());
        doNothing().when(flight).setSeatsLeft(anyInt());
        flight.setArrival("Amsterdam");
        flight.setDeparture("Istanbul");
        flight.setFlightNumber("42");
        flight.setId(1L);
        flight.setMaxSeats(3);
        flight.setSeatPrice(BigDecimal.valueOf(42L));
        flight.setSeatsLeft(1);
        when(flightRepository.save(Mockito.<Flight>any())).thenReturn(flight);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<Object>>any())).thenReturn(null);

        FlightDto flightDto = new FlightDto();
        flightDto.setArrival("Amsterdam");
        flightDto.setDeparture("Istanbul");
        flightDto.setFlightNumber("42");
        flightDto.setId(1L);
        flightDto.setMaxSeats(3);
        flightDto.setSeatPrice(BigDecimal.valueOf(42L));
        flightDto.setSeatsLeft(1);
        assertNull(flightServiceImpl.createFlight(flightDto));
        verify(flightRepository).save(Mockito.<Flight>any());
        verify(flight).setArrival(Mockito.<String>any());
        verify(flight).setDeparture(Mockito.<String>any());
        verify(flight).setFlightNumber(Mockito.<String>any());
        verify(flight).setId(Mockito.<Long>any());
        verify(flight).setMaxSeats(anyInt());
        verify(flight).setSeatPrice(Mockito.<BigDecimal>any());
        verify(flight).setSeatsLeft(anyInt());
        verify(modelMapper, atLeast(1)).map(Mockito.<Object>any(), Mockito.<Class<Flight>>any());
    }


    @Test
    void testGetAllFlights() {
        ArrayList<Flight> content = new ArrayList<>();
        when(flightRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(content));
        PaginatedResponse<FlightDto> actualAllFlights = flightServiceImpl.getAllFlights(1, 3, "id");
        assertEquals(content, actualAllFlights.getContent());
        assertTrue(actualAllFlights.isLast());
        assertEquals(0L, actualAllFlights.getTotalElements());
        assertEquals(0, actualAllFlights.getPageSize());
        assertEquals(0, actualAllFlights.getPageNo());
        verify(flightRepository).findAll(Mockito.<Pageable>any());
    }

    @Test
    void testGetAllFlights2() {
        Flight flight = new Flight();
        flight.setArrival("Amsterdam");
        flight.setDeparture("Istanbul");
        flight.setFlightNumber("42");
        flight.setId(1L);
        flight.setMaxSeats(3);
        flight.setSeatPrice(BigDecimal.valueOf(42L));
        flight.setSeatsLeft(1);

        ArrayList<Flight> content = new ArrayList<>();
        content.add(flight);
        PageImpl<Flight> pageImpl = new PageImpl<>(content);
        when(flightRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);

        FlightDto flightDto = new FlightDto();
        flightDto.setArrival("Amsterdam");
        flightDto.setDeparture("Istanbul");
        flightDto.setFlightNumber("42");
        flightDto.setId(1L);
        flightDto.setMaxSeats(3);
        flightDto.setSeatPrice(BigDecimal.valueOf(42L));
        flightDto.setSeatsLeft(1);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<FlightDto>>any())).thenReturn(flightDto);
        PaginatedResponse<FlightDto> actualAllFlights = flightServiceImpl.getAllFlights(1, 3, "id");
        assertEquals(1, actualAllFlights.getContent().size());
        assertTrue(actualAllFlights.isLast());
        assertEquals(1L, actualAllFlights.getTotalElements());
        assertEquals(1, actualAllFlights.getPageSize());
        assertEquals(0, actualAllFlights.getPageNo());
        verify(flightRepository).findAll(Mockito.<Pageable>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<FlightDto>>any());
    }


    @Test
    void testGetAllFlights3() {
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
        flight2.setFlightNumber("55");
        flight2.setId(2L);
        flight2.setMaxSeats(1);
        flight2.setSeatPrice(BigDecimal.valueOf(42L));
        flight2.setSeatsLeft(0);

        ArrayList<Flight> content = new ArrayList<>();
        content.add(flight2);
        content.add(flight);
        PageImpl<Flight> pageImpl = new PageImpl<>(content);
        when(flightRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);

        FlightDto flightDto = new FlightDto();
        flightDto.setArrival("Amsterdam");
        flightDto.setDeparture("Istanbul");
        flightDto.setFlightNumber("42");
        flightDto.setId(1L);
        flightDto.setMaxSeats(3);
        flightDto.setSeatPrice(BigDecimal.valueOf(42L));
        flightDto.setSeatsLeft(1);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<FlightDto>>any())).thenReturn(flightDto);
        PaginatedResponse<FlightDto> actualAllFlights = flightServiceImpl.getAllFlights(1, 3, "id");
        assertEquals(2, actualAllFlights.getContent().size());
        assertTrue(actualAllFlights.isLast());
        assertEquals(2L, actualAllFlights.getTotalElements());
        assertEquals(2, actualAllFlights.getPageSize());
        assertEquals(0, actualAllFlights.getPageNo());
        verify(flightRepository).findAll(Mockito.<Pageable>any());
        verify(modelMapper, atLeast(1)).map(Mockito.<Object>any(), Mockito.<Class<FlightDto>>any());
    }


    @Test
    void testFindFlightById() {
        Flight flight = new Flight();
        flight.setArrival("Amsterdam");
        flight.setDeparture("Istanbul");
        flight.setFlightNumber("42");
        flight.setId(1L);
        flight.setMaxSeats(3);
        flight.setSeatPrice(BigDecimal.valueOf(42L));
        flight.setSeatsLeft(1);
        Optional<Flight> ofResult = Optional.of(flight);
        when(flightRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        FlightDto flightDto = new FlightDto();
        flightDto.setArrival("Amsterdam");
        flightDto.setDeparture("Istanbul");
        flightDto.setFlightNumber("42");
        flightDto.setId(1L);
        flightDto.setMaxSeats(3);
        flightDto.setSeatPrice(BigDecimal.valueOf(42L));
        flightDto.setSeatsLeft(1);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<FlightDto>>any())).thenReturn(flightDto);
        FlightDto actualFindFlightByIdResult = flightServiceImpl.findFlightById(1L);
        assertSame(flightDto, actualFindFlightByIdResult);
        assertEquals("42", actualFindFlightByIdResult.getSeatPrice().toString());
        verify(flightRepository).findById(Mockito.<Long>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<FlightDto>>any());
    }


    @Test
    void testFindFlightById2() {
        Flight flight = new Flight();
        flight.setArrival("Amsterdam");
        flight.setDeparture("Istanbul");
        flight.setFlightNumber("42");
        flight.setId(1L);
        flight.setMaxSeats(3);
        flight.setSeatPrice(BigDecimal.valueOf(42L));
        flight.setSeatsLeft(1);
        Optional<Flight> ofResult = Optional.of(flight);
        when(flightRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<FlightDto>>any()))
                .thenThrow(new ResourceNotFoundException("Flight", "id", 42L));
        assertThrows(ResourceNotFoundException.class, () -> flightServiceImpl.findFlightById(1L));
        verify(flightRepository).findById(Mockito.<Long>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<FlightDto>>any());
    }


    @Test
    void testUpdateFlight() {
        Flight flight = new Flight();
        flight.setArrival("Amsterdam");
        flight.setDeparture("Istanbul");
        flight.setFlightNumber("42");
        flight.setId(1L);
        flight.setMaxSeats(3);
        flight.setSeatPrice(BigDecimal.valueOf(42L));
        flight.setSeatsLeft(1);
        Optional<Flight> ofResult = Optional.of(flight);

        Flight flight2 = new Flight();
        flight2.setArrival("Amsterdam");
        flight2.setDeparture("Istanbul");
        flight2.setFlightNumber("42");
        flight2.setId(1L);
        flight2.setMaxSeats(3);
        flight2.setSeatPrice(BigDecimal.valueOf(42L));
        flight2.setSeatsLeft(1);
        when(flightRepository.save(Mockito.<Flight>any())).thenReturn(flight2);
        when(flightRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        FlightDto flightDto = new FlightDto();
        flightDto.setArrival("Amsterdam");
        flightDto.setDeparture("Istanbul");
        flightDto.setFlightNumber("42");
        flightDto.setId(1L);
        flightDto.setMaxSeats(3);
        flightDto.setSeatPrice(BigDecimal.valueOf(42L));
        flightDto.setSeatsLeft(1);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<FlightDto>>any())).thenReturn(flightDto);

        FlightDto flightDto2 = new FlightDto();
        flightDto2.setArrival("Amsterdam");
        flightDto2.setDeparture("Istanbul");
        flightDto2.setFlightNumber("42");
        flightDto2.setId(1L);
        flightDto2.setMaxSeats(3);
        flightDto2.setSeatPrice(BigDecimal.valueOf(42L));
        flightDto2.setSeatsLeft(1);
        FlightDto actualUpdateFlightResult = flightServiceImpl.updateFlight(flightDto2, 1L);
        assertSame(flightDto, actualUpdateFlightResult);
        assertEquals("42", actualUpdateFlightResult.getSeatPrice().toString());
        verify(flightRepository).save(Mockito.<Flight>any());
        verify(flightRepository).findById(Mockito.<Long>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<FlightDto>>any());
    }


    @Test
    void testUpdateFlight2() {
        Flight flight = new Flight();
        flight.setArrival("Amsterdam");
        flight.setDeparture("Istanbul");
        flight.setFlightNumber("42");
        flight.setId(1L);
        flight.setMaxSeats(3);
        flight.setSeatPrice(BigDecimal.valueOf(42L));
        flight.setSeatsLeft(1);
        Optional<Flight> ofResult = Optional.of(flight);

        Flight flight2 = new Flight();
        flight2.setArrival("Amsterdam");
        flight2.setDeparture("Istanbul");
        flight2.setFlightNumber("42");
        flight2.setId(1L);
        flight2.setMaxSeats(3);
        flight2.setSeatPrice(BigDecimal.valueOf(42L));
        flight2.setSeatsLeft(1);
        when(flightRepository.save(Mockito.<Flight>any())).thenReturn(flight2);
        when(flightRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<FlightDto>>any()))
                .thenThrow(new ResourceNotFoundException("Flight", "id", 42L));

        FlightDto flightDto = new FlightDto();
        flightDto.setArrival("Amsterdam");
        flightDto.setDeparture("Istanbul");
        flightDto.setFlightNumber("42");
        flightDto.setId(1L);
        flightDto.setMaxSeats(3);
        flightDto.setSeatPrice(BigDecimal.valueOf(42L));
        flightDto.setSeatsLeft(1);
        assertThrows(ResourceNotFoundException.class, () -> flightServiceImpl.updateFlight(flightDto, 1L));
        verify(flightRepository).save(Mockito.<Flight>any());
        verify(flightRepository).findById(Mockito.<Long>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<FlightDto>>any());
    }



    @Test
    void testDeleteFlightById() {
        Flight flight = new Flight();
        flight.setArrival("Amsterdam");
        flight.setDeparture("Istanbul");
        flight.setFlightNumber("42");
        flight.setId(1L);
        flight.setMaxSeats(3);
        flight.setSeatPrice(BigDecimal.valueOf(42L));
        flight.setSeatsLeft(1);
        Optional<Flight> ofResult = Optional.of(flight);
        doNothing().when(flightRepository).delete(Mockito.<Flight>any());
        when(flightRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        flightServiceImpl.deleteFlightById(1L);
        verify(flightRepository).findById(Mockito.<Long>any());
        verify(flightRepository).delete(Mockito.<Flight>any());
    }


    @Test
    void testDeleteFlightById2() {
        Flight flight = new Flight();
        flight.setArrival("Amsterdam");
        flight.setDeparture("Istanbul");
        flight.setFlightNumber("42");
        flight.setId(1L);
        flight.setMaxSeats(3);
        flight.setSeatPrice(BigDecimal.valueOf(42L));
        flight.setSeatsLeft(1);
        Optional<Flight> ofResult = Optional.of(flight);
        doThrow(new ResourceNotFoundException("Flight", "id", 42L)).when(flightRepository)
                .delete(Mockito.<Flight>any());
        when(flightRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> flightServiceImpl.deleteFlightById(1L));
        verify(flightRepository).findById(Mockito.<Long>any());
        verify(flightRepository).delete(Mockito.<Flight>any());
    }



}

