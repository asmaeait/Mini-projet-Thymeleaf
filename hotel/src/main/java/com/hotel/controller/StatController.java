/**
 * Ce contrôleur gère la partie analytique (Dashboard métier) de l'hôtel.
 * Il regroupe toutes les métriques de performance : Taux d'occupation,
 * Revenus financiers basés sur les réservations et le calcul du RevPAR.
 * 
 * Ce module est essentiel pour le suivi directionnel de l'établissement hôtelier.
 */
package com.hotel.controller;

import com.hotel.model.Chambre;
import com.hotel.model.Reservation;
import com.hotel.repository.ChambreRepository;
import com.hotel.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatController {

    private final ChambreRepository chambreRepo;
    private final ReservationRepository reservationRepo;

    @GetMapping
    public String afficherStatistiques(Model model) {
        // 1. Récupération des données brutes en base
        // On remonte la totalité de l'inventaire des chambres et l'historique complet des réservations
        List<Chambre> toutesChambres = chambreRepo.findAll();
        List<Reservation> toutesReservations = reservationRepo.findAll();

        int totalChambres = toutesChambres.size();
        
        // 2. Calcul du nombre de chambres actuellement bloquées / occupées
        // On filtre le flux pour ne garder que celles dont le flag disponible est à 'false'
        long chambresOccupees = toutesChambres.stream()
                .filter(c -> !c.isDisponible())
                .count();
        
        // 3. Formule standard du Taux d'occupation hôtelier (%)
        // Protège contre la division par zéro s'il n'y a pas encore de chambre créée
        double tauxOccupation = 0.0;
        if (totalChambres > 0) {
            tauxOccupation = ((double) chambresOccupees / totalChambres) * 100;
        }

        // 4. Agrégation Financière : Calcul d'exploitation 
        // On n'intègre que les réservations au statut 'CONFIRMEE' pour garantir la véracité du chiffre d'affaires
        double revenuTotal = toutesReservations.stream()
                .filter(r -> "CONFIRMEE".equals(r.getStatut()))
                .mapToDouble(Reservation::getTotal) // On fait la somme des montants totaux calculés
                .sum();

        // 5. RevPAR Global (Revenue Per Available Room)
        // C'est l'indicateur clé du monde de l'hôtellerie combinant le taux d'occupation et le prix moyen
        double revPar = 0.0;
        if (totalChambres > 0) {
            revPar = revenuTotal / totalChambres;
        }

        // 6. Analyse de Segmentation : RevPAR détaillé selon la catégorie de chambre (Suite, Double, etc.)
        java.util.Map<String, Double> revparMap = new java.util.HashMap<>();
        
        // Regroupement pour compter le volume de chambres pour chaque type
        java.util.Map<String, Long> chambresParType = toutesChambres.stream()
                .collect(java.util.stream.Collectors.groupingBy(Chambre::getType, java.util.stream.Collectors.counting()));
                
        // Regroupement pour faire la somme des revenus générés pour chaque type de chambre (uniquement pour les confirmées)
        java.util.Map<String, Double> revenuParType = toutesReservations.stream()
                .filter(r -> "CONFIRMEE".equals(r.getStatut()) && r.getChambre() != null)
                .collect(java.util.stream.Collectors.groupingBy(
                        r -> r.getChambre().getType(),
                        java.util.stream.Collectors.summingDouble(Reservation::getTotal)
                ));

        // Cross-tabulation: on croise le revenu par type avec la volumétrie par type pour finaliser le RevPAR spécifique
        for (java.util.Map.Entry<String, Long> entry : chambresParType.entrySet()) {
            String typeChambre = entry.getKey();
            Long count = entry.getValue();
            Double revenu = revenuParType.getOrDefault(typeChambre, 0.0);
            revparMap.put(typeChambre, count > 0 ? revenu / count : 0.0);
        }

        // 7. Injection des KPIs dans le Modèle de la Vue (Moteur de rendu Thymeleaf)
        // Les Math.round sont utilisés pour avoir un affichage financier propre à deux décimales
        model.addAttribute("totalChambres", totalChambres);
        model.addAttribute("chambresOccupees", chambresOccupees);
        model.addAttribute("tauxOccupation", Math.round(tauxOccupation * 100.0) / 100.0);
        model.addAttribute("revenuTotal", revenuTotal);
        model.addAttribute("revPar", Math.round(revPar * 100.0) / 100.0);
        model.addAttribute("totalReservations", toutesReservations.size());
        model.addAttribute("revpar", revparMap); // Map qui sera itérée dans un tableau analytique côté front

        return "reservations/stats"; // Envoi final au template chargé de la restitution graphique
    }
}
