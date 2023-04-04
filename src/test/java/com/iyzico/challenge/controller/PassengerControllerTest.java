package com.iyzico.challenge.controller;

import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iyzico.challenge.dto.PaginatedResponse;
import com.iyzico.challenge.dto.PassengerDto;
import com.iyzico.challenge.service.PassengerService;
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

@ContextConfiguration(classes = {PassengerController.class})
@ExtendWith(SpringExtension.class)
class PassengerControllerTest {
    @Autowired
    private PassengerController passengerController;

    @MockBean
    private PassengerService passengerService;

    @Test
    void testGetAllPassengers() throws Exception {
        when(passengerService.getAllPassengers(anyInt(), anyInt(), Mockito.<String>any()))
                .thenReturn(new PaginatedResponse<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/passenger");
        MockMvcBuilders.standaloneSetup(passengerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"content\":null,\"pageNo\":0,\"pageSize\":0,\"totalElements\":0,\"totalPages\":0,\"last\":false}"));
    }


    @Test
    void testCreatePassenger() throws Exception {
        when(passengerService.getAllPassengers(anyInt(), anyInt(), Mockito.<String>any()))
                .thenReturn(new PaginatedResponse<>());

        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setAge(1);
        passengerDto.setEmail("gunduc@gmail.com");
        passengerDto.setId(123L);
        passengerDto.setName("Cengiz");
        passengerDto.setSurname("Gunduc");
        String content = (new ObjectMapper()).writeValueAsString(passengerDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/passenger")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(passengerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"content\":null,\"pageNo\":0,\"pageSize\":0,\"totalElements\":0,\"totalPages\":0,\"last\":false}"));
    }


    @Test
    void testFindPassengerById() throws Exception {
        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setAge(1);
        passengerDto.setEmail("gunduc@gmail.com");
        passengerDto.setId(123L);
        passengerDto.setName("Cengiz");
        passengerDto.setSurname("Gunduc");
        when(passengerService.findPassengerById(anyLong())).thenReturn(passengerDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/passenger/{id}", 1L);
        MockMvcBuilders.standaloneSetup(passengerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"name\":\"Cengiz\",\"surname\":\"Gunduc\",\"email\":\"gunduc@gmail.com\",\"age\":1,\"id\":123}"));
    }


    @Test
    void testUpdatePassenger() throws Exception {
        PassengerDto passengerDto = new PassengerDto();
        passengerDto.setAge(1);
        passengerDto.setEmail("gunduc@gmail.com");
        passengerDto.setId(123L);
        passengerDto.setName("Cengiz");
        passengerDto.setSurname("Gunduc");
        when(passengerService.updatePassenger(Mockito.<PassengerDto>any(), anyLong())).thenReturn(passengerDto);

        PassengerDto passengerDto2 = new PassengerDto();
        passengerDto2.setAge(1);
        passengerDto2.setEmail("gunduc@gmail.com");
        passengerDto2.setId(123L);
        passengerDto2.setName("Cengiz");
        passengerDto2.setSurname("Gunduc");
        String content = (new ObjectMapper()).writeValueAsString(passengerDto2);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/passenger/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(passengerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"name\":\"Cengiz\",\"surname\":\"Gunduc\",\"email\":\"gunduc@gmail.com\",\"age\":1,\"id\":123}"));
    }
}

