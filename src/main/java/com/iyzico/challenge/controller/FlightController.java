package com.iyzico.challenge.controller;

import com.iyzico.challenge.dto.FlightDto;
import com.iyzico.challenge.dto.PaginatedResponse;
import com.iyzico.challenge.service.FlightService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/flight")
public class FlightController {

    private FlightService flightService;

    public FlightController(FlightService flightService){
        this.flightService = flightService;
    }

    @PostMapping
    public ResponseEntity<FlightDto> createFlight(@RequestBody @Valid FlightDto flightDto){
        return new ResponseEntity<FlightDto>(flightService.createFlight(flightDto), HttpStatus.CREATED);
    }

    @GetMapping
    public PaginatedResponse<FlightDto> getAllFlights(
            @RequestParam(value="pageNo", defaultValue = "0",required = false) int pageNo,
            @RequestParam(value="pageSize", defaultValue = "10",required = false) int pageSize,
            @RequestParam(value="sortBy", defaultValue = "id",required = false) String sortBy
    ){
        return flightService.getAllFlights(pageNo,pageSize,sortBy);
    }

    @GetMapping("/{id}")
    public FlightDto findFlightById(@PathVariable(name="id") long id){
        return flightService.findFlightById(id);
    }

    @PutMapping("/{id}")
    public FlightDto updateFlight(@RequestBody @Valid FlightDto flightDto, @PathVariable(name="id") long id){
        return flightService.updateFlight(flightDto,id);
    }

}
