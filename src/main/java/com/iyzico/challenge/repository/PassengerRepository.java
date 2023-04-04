package com.iyzico.challenge.repository;

import com.iyzico.challenge.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger,Long> {
}
