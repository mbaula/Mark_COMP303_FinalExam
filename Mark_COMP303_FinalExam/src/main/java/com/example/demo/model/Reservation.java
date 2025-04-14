package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;

@Document(collection = "reservations")
@JsonInclude(JsonInclude.Include.NON_NULL) // only include non null in our response
public class Reservation {
	@Id
	private String id;
    private String details;
    private String ticketNumber;
    private String date;

    private String dateOfDeparture;
    private String timeOfDeparture;

    private Customer customer;
    private AirlineTicket airlineTicket;
    
    @Transient
    private Integer day;
    @Transient
    private Integer month;
    @Transient
    private Integer year;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDateOfDeparture() {
		return dateOfDeparture;
	}
	public void setDateOfDeparture(String dateOfDeparture) {
		this.dateOfDeparture = dateOfDeparture;
	}
	public String getTimeOfDeparture() {
		return timeOfDeparture;
	}
	public void setTimeOfDeparture(String timeOfDeparture) {
		this.timeOfDeparture = timeOfDeparture;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public AirlineTicket getAirlineTicket() {
		return airlineTicket;
	}
	public void setAirlineTicket(AirlineTicket airlineTicket) {
		this.airlineTicket = airlineTicket;
	}
	public Integer getDay() {
		return day;
	}
	public void setDay(Integer day) {
		this.day = day;
	}
	public Integer getMonth() {
		return month;
	}
	public void setMonth(Integer month) {
		this.month = month;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
	
}
