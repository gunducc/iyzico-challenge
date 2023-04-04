package com.iyzico.challenge.service.impl;

import com.iyzico.challenge.dto.FlightDto;
import com.iyzico.challenge.dto.PaginatedResponse;
import com.iyzico.challenge.entity.Flight;
import com.iyzico.challenge.exception.ResourceNotFoundException;
import com.iyzico.challenge.repository.FlightRepository;
import com.iyzico.challenge.service.FlightService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightServiceImpl implements FlightService {

    private FlightRepository flightRepository;

    private ModelMapper mapper;

    public FlightServiceImpl(FlightRepository flightRepository, ModelMapper mapper){
        this.flightRepository = flightRepository;
        this.mapper = mapper;
    }

    @Override
    public FlightDto createFlight(FlightDto flightDto) {
        Flight flight = mapToEntity(flightDto);
        Flight newFlight = flightRepository.save(flight);
        return mapToDTO(newFlight);
    }

    @Override
    public PaginatedResponse<FlightDto> getAllFlights(int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Flight> flights = flightRepository.findAll(pageable);
        List<Flight> flightList = flights.getContent();
        List<FlightDto> content = flightList.stream().map(flight -> mapToDTO(flight)).collect(Collectors.toList());

        PaginatedResponse<FlightDto> flightResponse = new PaginatedResponse<FlightDto>();
        flightResponse.setContent(content);
        flightResponse.setPageSize(flights.getSize());
        flightResponse.setPageNo(flights.getNumber());
        flightResponse.setTotalElements(flights.getTotalElements());
        flightResponse.setLast(flights.isLast());

        return flightResponse;
    }

    @Override
    public FlightDto findFlightById(long id) {
        return mapToDTO(flightRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Flight","id",id)));
    }

    @Override
    public FlightDto updateFlight(FlightDto flightDto, long id) {
        Flight flight = flightRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Flight","id",id));
        flight.setFlightNumber(flightDto.getFlightNumber());
        flight.setDeparture(flightDto.getDeparture());
        flight.setArrival(flightDto.getArrival());
        flight.setSeatPrice(flightDto.getSeatPrice());
        flight.setSeatsLeft(flightDto.getSeatsLeft());
        flight.setMaxSeats(flightDto.getMaxSeats());
        return mapToDTO(flightRepository.save(flight));
    }

    @Override
    public void deleteFlightById(long id) {
        Flight flight = flightRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Flight","id",id));
        flightRepository.delete(flight);
    }

    private FlightDto mapToDTO(Flight flight){
        FlightDto flightDto = mapper.map(flight, FlightDto.class);
        return flightDto;
    }

    private Flight mapToEntity(FlightDto flightDto){
        Flight flight = mapper.map(flightDto, Flight.class);
        return flight;
    }
}
