package com.hotel.controller;

import com.hotel.model.Chambre;
import com.hotel.repository.ChambreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Contrôleur qui gère toutes les actions liées aux chambres
@Controller
@RequestMapping("/chambres")
@RequiredArgsConstructor
public class ChambreController {

    private final ChambreRepository chambreRepo;

    // Affiche la liste des chambres avec filtres optionnels
    @GetMapping
    public String lister(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String disponible,
            Model model) {

        List<Chambre> chambres;

        // On applique le filtre selon ce que l'utilisateur a choisi
        if (type != null && !type.isEmpty() && disponible != null && !disponible.isEmpty()) {
            chambres = chambreRepo.findByTypeAndDisponible(type, Boolean.parseBoolean(disponible));
        } else if (type != null && !type.isEmpty()) {
            chambres = chambreRepo.findByType(type);
        } else if (disponible != null && !disponible.isEmpty()) {
            chambres = chambreRepo.findByDisponible(Boolean.parseBoolean(disponible));
        } else {
            chambres = chambreRepo.findAll();
        }

        model.addAttribute("chambres", chambres);
        model.addAttribute("typeFiltre", type);
        model.addAttribute("disponibleFiltre", disponible);
        return "chambres/liste";
    }

    // Affiche le formulaire pour ajouter une nouvelle chambre
    @GetMapping("/nouveau")
    public String formulaireAjout(Model model) {
        model.addAttribute("chambre", new Chambre());
        return "chambres/form";
    }

    // Affiche le formulaire pour modifier une chambre existante
    @GetMapping("/modifier/{id}")
    public String formulaireModifier(@PathVariable Long id, Model model) {
        Chambre chambre = chambreRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Chambre non trouvee"));
        model.addAttribute("chambre", chambre);
        return "chambres/form";
    }

    // Sauvegarde la chambre (ajout ou modification)
    @PostMapping("/sauvegarder")
    public String sauvegarder(@ModelAttribute Chambre chambre) {
        chambreRepo.save(chambre);
        return "redirect:/chambres";
    }

    // Supprime une chambre par son identifiant
    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id) {
        chambreRepo.deleteById(id);
        return "redirect:/chambres";
    }
}
