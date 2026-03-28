package com.hotel.controller;

import com.hotel.model.Client;
import com.hotel.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientRepository clientRepo;

    @GetMapping
    public String lister(Model model) {
        model.addAttribute("clients", clientRepo.findAll());
        return "clients/liste";
    }

    @GetMapping("/nouveau")
    public String formulaireAjout(Model model) {
        model.addAttribute("client", new Client());
        return "clients/form";
    }

    @GetMapping("/modifier/{id}")
    public String formulaireModifier(@PathVariable Long id, Model model) {
        Client client = clientRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        model.addAttribute("client", client);
        return "clients/form";
    }

    @PostMapping("/sauvegarder")
    public String sauvegarder(@ModelAttribute Client client) {
        clientRepo.save(client);
        return "redirect:/clients";
    }

    @GetMapping("/supprimer/{id}")
    public String supprimer(@PathVariable Long id) {
        clientRepo.deleteById(id);
        return "redirect:/clients";
    }
}