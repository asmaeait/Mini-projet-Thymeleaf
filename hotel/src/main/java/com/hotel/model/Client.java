/**
 * Entité métier représentant un Client de l'hôtel.
 * Ce modèle stocke les informations personnelles nécessaires 
 * à l'enregistrement (Check-in) et à la facturation.
 * 
 * Un client peut avoir plusieurs réservations rattachées à son profil.
 */
package com.hotel.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    // L'ID auto-généré sert de numéro de dossier unique pour chaque client
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nom complet du client, utilisé pour l'affichage et la recherche rapide
    private String nom;

    // Document d'identification légal requis par les autorités pour tout séjour hôtelier
    private String passeport;
}
