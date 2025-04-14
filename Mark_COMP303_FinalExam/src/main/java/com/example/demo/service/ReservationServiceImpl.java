package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.AirlineTicket;
import com.example.demo.model.Customer;
import com.example.demo.model.Reservation;
import com.example.demo.repository.AirlineTicketRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.ReservationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ReservationServiceImpl implements ReservationService {
	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AirlineTicketRepository ticketRepository;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	// We manually use Jackson’s ObjectMapper to log and verify serialization/deserialization
    // Though Spring Boot auto-handles JSON mapping, this shows explicit Jackson usage
    // Demonstrates full Jackson cycle: Java → JSON → Java
	// https://docs.spring.io/spring-boot/reference/features/json.html
	@Override
    public Reservation saveReservationWithEntities(Reservation reservation) {
		// Manually log Jackson serialization 
        try {
            String incomingJson = objectMapper.writeValueAsString(reservation);
            System.out.println("=== Incoming Reservation (Serialized by Jackson) ===");
            System.out.println(incomingJson);

            Reservation parsed = objectMapper.readValue(incomingJson, Reservation.class);
            System.out.println("=== Deserialized Object (Jackson again) ===");
            System.out.println("Parsed Customer: " + parsed.getCustomer().getFirstName());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        
        AirlineTicket ticket = reservation.getAirlineTicket();
        ticket.setTicketNumber("HT" + (int) (Math.random() * 1_000_00000));
        double unit = 100 + Math.random() * 100;
        ticket.setPrice(String.format("$%.2f", unit * ticket.getNumberOfPassengers()));
        AirlineTicket savedTicket = ticketRepository.save(ticket);

        Customer customer = reservation.getCustomer();
        customer.setReservation(null); 
        Customer savedCustomer = customerRepository.save(customer);

        reservation.setCustomer(savedCustomer);
        reservation.setAirlineTicket(savedTicket);
        Reservation savedReservation = reservationRepository.save(reservation);

        savedCustomer.setReservation(savedReservation.getId());
        customerRepository.save(savedCustomer);

        return savedReservation;
    }
	
	@Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
	
	@Override
	public Reservation updateReservation(String id, Reservation updatedReservation) {
	    Reservation existing = reservationRepository.findById(id)
	        .orElseThrow(() -> new RuntimeException("Reservation not found with ID: " + id));

	    Customer updatedCustomer = updatedReservation.getCustomer();
	    if (updatedCustomer != null) {
	        Customer existingCustomer = existing.getCustomer();
	        existingCustomer.setFirstName(updatedCustomer.getFirstName());
	        existingCustomer.setLastName(updatedCustomer.getLastName());
	        existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());
	        customerRepository.save(existingCustomer);
	    }

	    AirlineTicket updatedTicket = updatedReservation.getAirlineTicket();
	    if (updatedTicket != null) {
	        AirlineTicket existingTicket = existing.getAirlineTicket();
	        existingTicket.setNumberOfPassengers(updatedTicket.getNumberOfPassengers());
	        existingTicket.setTravelClass(updatedTicket.getTravelClass());
	        ticketRepository.save(existingTicket);
	    }

	    existing.setDay(updatedReservation.getDay());
	    existing.setMonth(updatedReservation.getMonth());
	    existing.setYear(updatedReservation.getYear());

	    return reservationRepository.save(existing);
	}
}