package com.hotel.controller;

import com.hotel.model.Reservation;
import com.hotel.repository.ChambreRepository;
import com.hotel.repository.ClientRepository;
import com.hotel.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationRepository reservationRepo;
    private final ChambreRepository chambreRepo;
    private final ClientRepository clientRepo;

    // Lister avec filtres optionnels
    @GetMapping
    public String lister(
            @RequestParam(required = false) String statut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
            Model model) {

        List<Reservation> reservations;

        if (statut != null && !statut.isEmpty() && debut != null && fin != null) {
            reservations = reservationRepo.findByStatutAndCheckInBetween(statut, debut, fin);
        } else if (statut != null && !statut.isEmpty()) {
            reservations = reservationRepo.findByStatut(statut);
        } else if (debut != null && fin != null) {
            reservations = reservationRepo.findByCheckInBetween(debut, fin);
        } else {
            reservations = reservationRepo.findAll();
        }

        model.addAttribute("reservations", reservations);
        model.addAttribute("statutFiltre", statut);
        model.addAttribute("debut", debut);
        model.addAttribute("fin", fin);
        return "reservations/liste";
    }

    // Formulaire d'ajout
    @GetMapping("/nouveau")
    public String formulaireAjout(Model model) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("chambres", chambreRepo.findAll());
        model.addAttribute("clients", clientRepo.findAll());
        return "reservations/form";
    }

    // Formulaire de modification
    @GetMapping("/modifier/{id}")
    public String formulaireModifier(@PathVariable Long id, Model model) {
        Reservation reservation = reservationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Réservation non trouvée"));
        model.addAttribute("reservation", reservation);
        model.addAttribute("chambres", chambreRepo.findAll());
        model.addAttribute("clients", clientRepo.findAll());
        return "reservations/form";
    }

    // Sauvegarder avec calcul automatique du total
    @PostMapping("/sauvegarder")
    public String sauvegarder(
            @ModelAttribute Reservation reservation,
            @RequestParam Long chambreId,
            @RequestParam Long clientId) {

        // On récupère la chambre et le client
        reservation.setChambre(chambreRepo.findById(chambreId).orElseThrow());
        reservation.setClient(clientRepo.findById(clientId).orElseThrow());

        // Calcul automatique du total
        reservation.calculerTotal();

        // Mise à jour de la disponibilité de la chambre
        if ("CONFIRMEE".equals(reservation.getStatut())) {
            reservation.getChambre().setDisponible(false);
            chambreRepo.save(reservation.getChambre());
        }

        reservationRepo.save(reservation);
        return "redirect:/reservations";
    }

    // Supprimer et rendre la chambre disponible
    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id) {
        Reservation reservation = reservationRepo.findById(id).orElseThrow();
        // Remettre la chambre disponible
        reservation.getChambre().setDisponible(true);
        chambreRepo.save(reservation.getChambre());
        reservationRepo.deleteById(id);
        return "redirect:/reservations";
    }
}