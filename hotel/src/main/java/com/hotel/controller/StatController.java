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
        List<Chambre> toutesChambres = chambreRepo.findAll();
        List<Reservation> toutesReservations = reservationRepo.findAll();

        int totalChambres = toutesChambres.size();
        
        // Taux d'occupation : (chambres occupées / total chambres) * 100
        long chambresOccupees = toutesChambres.stream()
                .filter(c -> !c.isDisponible())
                .count();
        
        double tauxOccupation = 0.0;
        if (totalChambres > 0) {
            tauxOccupation = ((double) chambresOccupees / totalChambres) * 100;
        }

        // Revenu total des réservations confirmées
        double revenuTotal = toutesReservations.stream()
                .filter(r -> "CONFIRMEE".equals(r.getStatut()))
                .mapToDouble(Reservation::getTotal)
                .sum();

        // RevPAR (Revenue Per Available Room)
        double revPar = 0.0;
        if (totalChambres > 0) {
            revPar = revenuTotal / totalChambres;
        }

        java.util.Map<String, Double> revparMap = new java.util.HashMap<>();
        java.util.Map<String, Long> chambresParType = toutesChambres.stream()
                .collect(java.util.stream.Collectors.groupingBy(Chambre::getType, java.util.stream.Collectors.counting()));
        java.util.Map<String, Double> revenuParType = toutesReservations.stream()
                .filter(r -> "CONFIRMEE".equals(r.getStatut()) && r.getChambre() != null)
                .collect(java.util.stream.Collectors.groupingBy(
                        r -> r.getChambre().getType(),
                        java.util.stream.Collectors.summingDouble(Reservation::getTotal)
                ));

        for (java.util.Map.Entry<String, Long> entry : chambresParType.entrySet()) {
            String typeChambre = entry.getKey();
            Long count = entry.getValue();
            Double revenu = revenuParType.getOrDefault(typeChambre, 0.0);
            revparMap.put(typeChambre, count > 0 ? revenu / count : 0.0);
        }

        model.addAttribute("totalChambres", totalChambres);
        model.addAttribute("chambresOccupees", chambresOccupees);
        model.addAttribute("tauxOccupation", Math.round(tauxOccupation * 100.0) / 100.0);
        model.addAttribute("revenuTotal", revenuTotal);
        model.addAttribute("revPar", Math.round(revPar * 100.0) / 100.0);
        model.addAttribute("totalReservations", toutesReservations.size());
        model.addAttribute("revpar", revparMap);

        return "reservations/stats";
    }
}
