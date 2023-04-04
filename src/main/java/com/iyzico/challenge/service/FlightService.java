package com.iyzico.challenge.service;

import com.iyzico.challenge.dto.FlightDto;
import com.iyzico.challenge.dto.PaginatedResponse;

public interface FlightService {

    FlightDto createFlight(FlightDto flightDto);

    PaginatedResponse<FlightDto> getAllFlights(int pageNo, int pageSize, String sortBy);

    FlightDto findFlightById(long id);

    FlightDto updateFlight(FlightDto flightDto, long id);

    void deleteFlightById(long id);

}
