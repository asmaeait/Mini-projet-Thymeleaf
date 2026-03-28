package com.hotel;

import com.hotel.model.Chambre;
import com.hotel.model.Client;
import com.hotel.model.Reservation;
import com.hotel.repository.ChambreRepository;
import com.hotel.repository.ClientRepository;
import com.hotel.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ChambreRepository chambreRepo;
    private final ClientRepository clientRepo;
    private final ReservationRepository reservationRepo;

    @Override
    public void run(String... args) {

        // Insertion des chambres
        Chambre c1 = new Chambre(null, "101", 1, "SINGLE", 60.0, true);
        Chambre c2 = new Chambre(null, "102", 1, "SINGLE", 65.0, true);
        Chambre c3 = new Chambre(null, "201", 2, "DOUBLE", 110.0, false);
        Chambre c4 = new Chambre(null, "202", 2, "DOUBLE", 120.0, true);
        Chambre c5 = new Chambre(null, "301", 3, "SUITE", 250.0, false);
        Chambre c6 = new Chambre(null, "302", 3, "SUITE", 280.0, true);

        chambreRepo.save(c1);
        chambreRepo.save(c2);
        chambreRepo.save(c3);
        chambreRepo.save(c4);
        chambreRepo.save(c5);
        chambreRepo.save(c6);

        // Insertion des clients
        Client cl1 = new Client(null, "Alice Martin", "FR1234567");
        Client cl2 = new Client(null, "Bob Dupont", "FR7654321");
        Client cl3 = new Client(null, "Sarah Leclerc", "BE9988776");
        Client cl4 = new Client(null, "Mohammed Amine", "MA1122334");

        clientRepo.save(cl1);
        clientRepo.save(cl2);
        clientRepo.save(cl3);
        clientRepo.save(cl4);

        // Réservation 1
        Reservation r1 = new Reservation();
        r1.setClient(cl1);
        r1.setChambre(c3);
        r1.setCheckIn(LocalDate.of(2025, 3, 10));
        r1.setCheckOut(LocalDate.of(2025, 3, 15));
        r1.setStatut("CONFIRMEE");
        r1.calculerTotal();
        reservationRepo.save(r1);

        // Réservation 2
        Reservation r2 = new Reservation();
        r2.setClient(cl2);
        r2.setChambre(c5);
        r2.setCheckIn(LocalDate.of(2025, 3, 20));
        r2.setCheckOut(LocalDate.of(2025, 3, 23));
        r2.setStatut("CONFIRMEE");
        r2.calculerTotal();
        reservationRepo.save(r2);

        // Réservation 3
        Reservation r3 = new Reservation();
        r3.setClient(cl3);
        r3.setChambre(c1);
        r3.setCheckIn(LocalDate.of(2025, 5, 1));
        r3.setCheckOut(LocalDate.of(2025, 5, 3));
        r3.setStatut("EN_ATTENTE");
        r3.calculerTotal();
        reservationRepo.save(r3);

        // Réservation 4
        Reservation r4 = new Reservation();
        r4.setClient(cl4);
        r4.setChambre(c4);
        r4.setCheckIn(LocalDate.of(2025, 4, 5));
        r4.setCheckOut(LocalDate.of(2025, 4, 7));
        r4.setStatut("ANNULEE");
        r4.calculerTotal();
        reservationRepo.save(r4);

        System.out.println("Hotel Royal - Donnees de test inserees !");
    }
}