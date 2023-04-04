package com.iyzico.challenge.service;

import com.iyzico.challenge.dto.PaginatedResponse;
import com.iyzico.challenge.dto.PassengerDto;

public interface PassengerService {

    PassengerDto createPassenger(PassengerDto passengerDto);

    PaginatedResponse<PassengerDto> getAllPassengers(int pageNo, int pageSize, String sortBy);

    PassengerDto findPassengerById(long id);

    PassengerDto updatePassenger(PassengerDto passengerDto, long id);

    void deletePassengerById(long id);

}
