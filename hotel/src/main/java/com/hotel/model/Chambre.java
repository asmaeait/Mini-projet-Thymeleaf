/**
 * Entité représentant une Chambre dans le système de gestion hôtelière.
 * Ce modèle gère les caractéristiques physiques (étage, numéro) 
 * ainsi que sa politique tarifaire et son statut d'occupation actuel.
 * 
 * Cette classe est mappée à une table en base de données grâce à l'annotation @Entity.
 */
package com.hotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chambre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identifiant unique généré automatiquement pour chaque chambre

    private String numero; // Numéro attribué à la chambre (ex: "101", "302")
    private int etage;     // Étage de localisation permettant de filtrer selon les préférences client
    private String type;   // Catégorie de la chambre (ex: SINGLE, DOUBLE, SUITE)
    
    // Tarif appliqué par nuitée, modulable selon les saisons via la logique d'affaires
    private double prixNuit; 
    
    // Indicateur en temps réel de disponibilité (true = libre, false = occupée)
    // Permet de prévenir la surréservation (double booking)
    private boolean disponible;
}