/**
 * Entité centrale du système : La Réservation.
 * Elle lie un Client à une Chambre pour une période donnée.
 * Ce modèle gère du cycle de vie de la réservation (statut)
 * et s'occupe du calcul financier du séjour.
 */
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
    private Long id; // Numéro de confirmation unique de la réservation

    // Période du séjour
    private LocalDate checkIn;  // Date prévue d'arrivée (à partir de 14h théoriquement)
    private LocalDate checkOut; // Date prévue de départ (avant 12h)

    // Cycle de validité : "CONFIRMEE", "EN_ATTENTE" (attente de paiement/validation), "ANNULEE" (libère l'inventaire)
    private String statut;

    // Montant total facturé au client pour cette réservation précise
    private double total;

    // Relation fonctionnelle : La chambre physique allouée à cette réservation
    @ManyToOne
    @JoinColumn(name = "chambre_id")
    private Chambre chambre;

    // Relation fonctionnelle : Le client titulaire / payeur
    @ManyToOne
    @JoinColumn(name = "client_id")
    private  Client client;

    /**
     * Règle de gestion financière : 
     * Calcule automatiquement le coût total du séjour basé sur la durée réelle.
     * Algorithme = (Date fin - Date début en jours) * Tarif de nuit de la chambre associée.
     */
    public void calculerTotal(){
        if (checkIn != null && checkOut != null && chambre != null){
            // Utilisation de ChronoUnit pour obtenir le nombre de nuitées exactes
            long nuits = ChronoUnit.DAYS.between(checkIn, checkOut);
            this.total = nuits * chambre.getPrixNuit();
        }
    }

}
