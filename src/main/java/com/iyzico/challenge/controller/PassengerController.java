package com.iyzico.challenge.controller;

import com.iyzico.challenge.dto.PaginatedResponse;
import com.iyzico.challenge.dto.PassengerDto;
import com.iyzico.challenge.entity.Passenger;
import com.iyzico.challenge.service.PassengerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/passenger")
public class PassengerController {

    private PassengerService passengerService;

    public PassengerController(PassengerService passengerService){
        this.passengerService = passengerService;
    }
    @PostMapping
    public ResponseEntity<PassengerDto> createPassenger(@RequestBody @Valid PassengerDto passengerDto){
        return new ResponseEntity<PassengerDto>(passengerService.createPassenger(passengerDto), HttpStatus.CREATED);
    }

    @GetMapping
    public PaginatedResponse<PassengerDto> getAllPassengers(
            @RequestParam(value="pageNo", defaultValue = "0",required = false) int pageNo,
            @RequestParam(value="pageSize", defaultValue = "10",required = false) int pageSize,
            @RequestParam(value="sortBy", defaultValue = "id",required = false) String sortBy
    ){
        return passengerService.getAllPassengers(pageNo,pageSize,sortBy);
    }

    @GetMapping("/{id}")
    public PassengerDto findPassengerById(@PathVariable(name="id") long id){
        return passengerService.findPassengerById(id);
    }

    @PutMapping("/{id}")
    public PassengerDto updatePassenger(@RequestBody @Valid PassengerDto passengerDto, @PathVariable(name="id") long id){
        return passengerService.updatePassenger(passengerDto,id);
    }



}
