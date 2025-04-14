package com.example.demo.controller;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.model.Reservation;
import com.example.demo.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/")
    public String loadForm(Model model) {
        model.addAttribute("reservation", new Reservation());
        return "ReservationForm";
    }

    @PostMapping("/submit-reservation")
    public String handleSubmit(@ModelAttribute Reservation reservation, Model model) {
        if (reservation.getDay() != null && reservation.getMonth() != null && reservation.getYear() != null) {
            reservation.setDateOfDeparture(
                reservation.getYear() + "-" +
                String.format("%02d", reservation.getMonth()) + "-" +
                String.format("%02d", reservation.getDay())
            );
        }

        Reservation saved = reservationService.saveReservationWithEntities(reservation);

        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(saved);
            System.out.println("Saved Reservation:\n" + json);
            model.addAttribute("submittedJson", json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "ReservationForm";
    }
    
    @PostMapping("/submit-static-json")
    public String handleStaticJsonSubmit(Model model) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = new ClassPathResource("static/static-reservation.json").getInputStream();
            Reservation reservation = mapper.readValue(is, Reservation.class);

            Reservation saved = reservationService.saveReservationWithEntities(reservation);

            model.addAttribute("reservation", saved);

            String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(saved);
            model.addAttribute("submittedJson", json);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("submittedJson", "Error reading static JSON.");
            model.addAttribute("reservation", new Reservation()); 
        }

        return "ReservationForm";
    }
}