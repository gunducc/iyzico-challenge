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

import com.iyzico.challenge.dto.PaginatedResponse;
import com.iyzico.challenge.dto.PassengerDto;
import com.iyzico.challenge.entity.Passenger;
import com.iyzico.challenge.exception.ResourceNotFoundException;
import com.iyzico.challenge.repository.PassengerRepository;

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

@ContextConfiguration(classes = {PassengerServiceImpl.class})
@ExtendWith(SpringExtension.class)
class PassengerServiceImplTest {
    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private PassengerRepository passengerRepository;

    @Autowired
    private PassengerServiceImpl passengerServiceImpl;


    @Test
    void testCreatePassenger() {
        Passenger passenger = new Passenger();
        passenger.setAge(1);
        passenger.setEmail("gunduc@gmail.com");
        passenger.setId(1L);
        passenger.setName("Cengiz");
        passenger.setSurname("Gunduc");
        when(passengerRepository.save(Mockito.<Passenger>any())).thenReturn(passenger);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<Object>>any()))
                .thenThrow(new ResourceNotFoundException("Passenger", "id", 42L));

        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setAge(1);
        passengerDto.setEmail("gunduc@gmail.com");
        passengerDto.setId(123L);
        passengerDto.setName("Cengiz");
        passengerDto.setSurname("Gunduc");
        assertThrows(ResourceNotFoundException.class, () -> passengerServiceImpl.createPassenger(passengerDto));
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<Passenger>>any());
    }

    @Test
    void testCreatePassenger2() {
        Passenger passenger = mock(Passenger.class);
        doNothing().when(passenger).setAge(anyInt());
        doNothing().when(passenger).setEmail(Mockito.<String>any());
        doNothing().when(passenger).setId(Mockito.<Long>any());
        doNothing().when(passenger).setName(Mockito.<String>any());
        doNothing().when(passenger).setSurname(Mockito.<String>any());
        passenger.setAge(1);
        passenger.setEmail("gunduc@gmail.com");
        passenger.setId(1L);
        passenger.setName("Cengiz");
        passenger.setSurname("Gunduc");
        when(passengerRepository.save(Mockito.<Passenger>any())).thenReturn(passenger);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<Object>>any())).thenReturn(null);

        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setAge(1);
        passengerDto.setEmail("gunduc@gmail.com");
        passengerDto.setId(123L);
        passengerDto.setName("Cengiz");
        passengerDto.setSurname("Gunduc");
        assertNull(passengerServiceImpl.createPassenger(passengerDto));
        verify(passengerRepository).save(Mockito.<Passenger>any());
        verify(passenger).setAge(anyInt());
        verify(passenger).setEmail(Mockito.<String>any());
        verify(passenger).setId(Mockito.<Long>any());
        verify(passenger).setName(Mockito.<String>any());
        verify(passenger).setSurname(Mockito.<String>any());
        verify(modelMapper, atLeast(1)).map(Mockito.<Object>any(), Mockito.<Class<Passenger>>any());
    }


    @Test
    void testGetAllPassengers() {
        ArrayList<Passenger> content = new ArrayList<>();
        when(passengerRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(content));
        PaginatedResponse<PassengerDto> actualAllPassengers = passengerServiceImpl.getAllPassengers(1, 3, "id");
        assertEquals(content, actualAllPassengers.getContent());
        assertTrue(actualAllPassengers.isLast());
        assertEquals(0L, actualAllPassengers.getTotalElements());
        assertEquals(0, actualAllPassengers.getPageSize());
        assertEquals(0, actualAllPassengers.getPageNo());
        verify(passengerRepository).findAll(Mockito.<Pageable>any());
    }


    @Test
    void testGetAllPassengers2() {
        Passenger passenger = new Passenger();
        passenger.setAge(1);
        passenger.setEmail("gunduc@gmail.com");
        passenger.setId(1L);
        passenger.setName("Cengiz");
        passenger.setSurname("Gunduc");

        ArrayList<Passenger> content = new ArrayList<>();
        content.add(passenger);
        PageImpl<Passenger> pageImpl = new PageImpl<>(content);
        when(passengerRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);

        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setAge(1);
        passengerDto.setEmail("gunduc@gmail.com");
        passengerDto.setId(123L);
        passengerDto.setName("Cengiz");
        passengerDto.setSurname("Gunduc");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<PassengerDto>>any())).thenReturn(passengerDto);
        PaginatedResponse<PassengerDto> actualAllPassengers = passengerServiceImpl.getAllPassengers(1, 3, "id");
        assertEquals(1, actualAllPassengers.getContent().size());
        assertTrue(actualAllPassengers.isLast());
        assertEquals(0, actualAllPassengers.getPageNo());
        assertEquals(1L, actualAllPassengers.getTotalElements());
        assertEquals(1, actualAllPassengers.getPageSize());
        verify(passengerRepository).findAll(Mockito.<Pageable>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<PassengerDto>>any());
    }

    @Test
    void testGetAllPassengers3() {
        Passenger passenger = new Passenger();
        passenger.setAge(1);
        passenger.setEmail("gunduc@gmail.com");
        passenger.setId(1L);
        passenger.setName("Cengiz");
        passenger.setSurname("Gunduc");

        Passenger passenger2 = new Passenger();
        passenger2.setAge(0);
        passenger2.setEmail("ahmetkaya@gmail.com");
        passenger2.setId(2L);
        passenger2.setName("Ahmet");
        passenger2.setSurname("Kaya");

        ArrayList<Passenger> content = new ArrayList<>();
        content.add(passenger2);
        content.add(passenger);
        PageImpl<Passenger> pageImpl = new PageImpl<>(content);
        when(passengerRepository.findAll(Mockito.<Pageable>any())).thenReturn(pageImpl);

        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setAge(1);
        passengerDto.setEmail("gunduc@gmail.com");
        passengerDto.setId(123L);
        passengerDto.setName("Cengiz");
        passengerDto.setSurname("Gunduc");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<PassengerDto>>any())).thenReturn(passengerDto);
        PaginatedResponse<PassengerDto> actualAllPassengers = passengerServiceImpl.getAllPassengers(1, 3, "id");
        assertEquals(2, actualAllPassengers.getContent().size());
        assertTrue(actualAllPassengers.isLast());
        assertEquals(0, actualAllPassengers.getPageNo());
        assertEquals(2L, actualAllPassengers.getTotalElements());
        assertEquals(2, actualAllPassengers.getPageSize());
        verify(passengerRepository).findAll(Mockito.<Pageable>any());
        verify(modelMapper, atLeast(1)).map(Mockito.<Object>any(), Mockito.<Class<PassengerDto>>any());
    }


    @Test
    void testFindPassengerById() {
        Passenger passenger = new Passenger();
        passenger.setAge(1);
        passenger.setEmail("gunduc@gmail.com");
        passenger.setId(1L);
        passenger.setName("Cengiz");
        passenger.setSurname("Gunduc");
        Optional<Passenger> ofResult = Optional.of(passenger);
        when(passengerRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setAge(1);
        passengerDto.setEmail("gunduc@gmail.com");
        passengerDto.setId(123L);
        passengerDto.setName("Cengiz");
        passengerDto.setSurname("Gunduc");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<PassengerDto>>any())).thenReturn(passengerDto);
        assertSame(passengerDto, passengerServiceImpl.findPassengerById(1L));
        verify(passengerRepository).findById(Mockito.<Long>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<PassengerDto>>any());
    }


    @Test
    void testFindPassengerById2() {
        Passenger passenger = new Passenger();
        passenger.setAge(1);
        passenger.setEmail("gunduc@gmail.com");
        passenger.setId(1L);
        passenger.setName("Cengiz");
        passenger.setSurname("Gunduc");
        Optional<Passenger> ofResult = Optional.of(passenger);
        when(passengerRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<PassengerDto>>any()))
                .thenThrow(new ResourceNotFoundException("Passenger", "id", 42L));
        assertThrows(ResourceNotFoundException.class, () -> passengerServiceImpl.findPassengerById(1L));
        verify(passengerRepository).findById(Mockito.<Long>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<PassengerDto>>any());
    }


    @Test
    void testUpdatePassenger() {
        Passenger passenger = new Passenger();
        passenger.setAge(1);
        passenger.setEmail("gunduc@gmail.com");
        passenger.setId(1L);
        passenger.setName("Cengiz");
        passenger.setSurname("Gunduc");
        Optional<Passenger> ofResult = Optional.of(passenger);

        Passenger passenger2 = new Passenger();
        passenger2.setAge(1);
        passenger2.setEmail("gunduc@gmail.com");
        passenger2.setId(1L);
        passenger2.setName("Cengiz");
        passenger2.setSurname("Gunduc");
        when(passengerRepository.save(Mockito.<Passenger>any())).thenReturn(passenger2);
        when(passengerRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setAge(1);
        passengerDto.setEmail("gunduc@gmail.com");
        passengerDto.setId(123L);
        passengerDto.setName("Cengiz");
        passengerDto.setSurname("Gunduc");
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<PassengerDto>>any())).thenReturn(passengerDto);

        PassengerDto passengerDto2 = new PassengerDto();
        passengerDto2.setAge(1);
        passengerDto2.setEmail("gunduc@gmail.com");
        passengerDto2.setId(123L);
        passengerDto2.setName("Cengiz");
        passengerDto2.setSurname("Gunduc");
        assertSame(passengerDto, passengerServiceImpl.updatePassenger(passengerDto2, 1L));
        verify(passengerRepository).save(Mockito.<Passenger>any());
        verify(passengerRepository).findById(Mockito.<Long>any());
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<PassengerDto>>any());
    }



    @Test
    void testDeletePassengerById() {
        Passenger passenger = new Passenger();
        passenger.setAge(1);
        passenger.setEmail("gunduc@gmail.com");
        passenger.setId(1L);
        passenger.setName("Cengiz");
        passenger.setSurname("Gunduc");
        Optional<Passenger> ofResult = Optional.of(passenger);
        doNothing().when(passengerRepository).delete(Mockito.<Passenger>any());
        when(passengerRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        passengerServiceImpl.deletePassengerById(1L);
        verify(passengerRepository).findById(Mockito.<Long>any());
        verify(passengerRepository).delete(Mockito.<Passenger>any());
    }


    @Test
    void testDeletePassengerById2() {
        Passenger passenger = new Passenger();
        passenger.setAge(1);
        passenger.setEmail("gunduc@gmail.com");
        passenger.setId(1L);
        passenger.setName("Cengiz");
        passenger.setSurname("Gunduc");
        Optional<Passenger> ofResult = Optional.of(passenger);
        doThrow(new ResourceNotFoundException("Passenger", "id", 42L)).when(passengerRepository)
                .delete(Mockito.<Passenger>any());
        when(passengerRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        assertThrows(ResourceNotFoundException.class, () -> passengerServiceImpl.deletePassengerById(1L));
        verify(passengerRepository).findById(Mockito.<Long>any());
        verify(passengerRepository).delete(Mockito.<Passenger>any());
    }


    @Test
    void testDeletePassengerById3() {
        doNothing().when(passengerRepository).delete(Mockito.<Passenger>any());
        when(passengerRepository.findById(Mockito.<Long>any())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> passengerServiceImpl.deletePassengerById(1L));
        verify(passengerRepository).findById(Mockito.<Long>any());
    }
}

