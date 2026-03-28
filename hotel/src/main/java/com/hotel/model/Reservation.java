package com.hotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate checkIn;
    private LocalDate checkOut;

    // "CONFIRMEE", "EN_ATTENTE", "ANNULEE"
    private String statut;

    private double total;

    @ManyToOne
    @JoinColumn(name = "chambre_id")
    private Chambre chambre;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private  Client client;

    // calcul automatiquement le cout total du sejour
    public void calculerTotal(){
        if (checkIn != null && checkOut != null && chambre != null){
            long nuits = ChronoUnit.DAYS.between(checkIn, checkOut);
            this.total = nuits * chambre.getPrixNuit();
        }
    }

}
