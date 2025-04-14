package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.model.AirlineTicket;

public interface AirlineTicketRepository extends MongoRepository<AirlineTicket, String> {
}
