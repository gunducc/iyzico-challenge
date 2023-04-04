package com.iyzico.challenge.service.impl;

import com.iyzico.challenge.dto.FlightDto;
import com.iyzico.challenge.dto.PaginatedResponse;
import com.iyzico.challenge.dto.PassengerDto;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.entity.Passenger;
import com.iyzico.challenge.exception.ResourceNotFoundException;
import com.iyzico.challenge.repository.PassengerRepository;
import com.iyzico.challenge.service.PassengerService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PassengerServiceImpl implements PassengerService {

    private ModelMapper mapper;

    private PassengerRepository passengerRepository;

    public PassengerServiceImpl(PassengerRepository passengerRepository, ModelMapper mapper){
        this.passengerRepository = passengerRepository;
        this.mapper = mapper;
    }

    @Override
    public PassengerDto createPassenger(PassengerDto passengerDto) {
        Passenger passenger = mapToEntity(passengerDto);
        Passenger newPassenger = passengerRepository.save(passenger);
        return mapToDTO(newPassenger);
    }

    @Override
    public PaginatedResponse<PassengerDto> getAllPassengers(int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Passenger> passengers = passengerRepository.findAll(pageable);
        List<Passenger> passengerList = passengers.getContent();
        List<PassengerDto> content = passengerList.stream().map(passenger -> mapToDTO(passenger)).collect(Collectors.toList());

        PaginatedResponse<PassengerDto> passengerResponse = new PaginatedResponse<PassengerDto>();
        passengerResponse.setContent(content);
        passengerResponse.setPageSize(passengers.getSize());
        passengerResponse.setPageNo(passengers.getNumber());
        passengerResponse.setTotalElements(passengers.getTotalElements());
        passengerResponse.setLast(passengers.isLast());
        return passengerResponse;

    }

    @Override
    public PassengerDto findPassengerById(long id) {
        return mapToDTO(passengerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Passenger","id", id)));
    }

    @Override
    public PassengerDto updatePassenger(PassengerDto passengerDto, long id) {
        Passenger passenger = passengerRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Passenger","id",id));
        passenger.setAge(passengerDto.getAge());
        passenger.setName(passengerDto.getName());
        passenger.setSurname(passengerDto.getSurname());
        passenger.setEmail(passengerDto.getEmail());
        return mapToDTO(passengerRepository.save(passenger));
    }

    @Override
    public void deletePassengerById(long id) {
        Passenger passenger = passengerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Passenger","id",id));
        passengerRepository.delete(passenger);
    }

    private PassengerDto mapToDTO(Passenger passenger){
        PassengerDto passengerDto = mapper.map(passenger, PassengerDto.class);
        return passengerDto;
    }

    private Passenger mapToEntity(PassengerDto passengerDto){
        Passenger passenger = mapper.map(passengerDto, Passenger.class);
        return passenger;
    }

}
