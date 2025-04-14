package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Reservation;

public interface ReservationService {
	Reservation saveReservationWithEntities(Reservation reservation);
    List<Reservation> getAllReservations();
    Reservation updateReservation(String id, Reservation updatedReservation);
}
