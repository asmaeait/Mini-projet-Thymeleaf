package com.hotel.repository;

import com.hotel.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long>{

    // Filter par statut
    List<Reservation> findByStatut(String statut);

    // Filtrer les reservations dans une periode (CheckIn entre deux dates)
    List<Reservation> findByCheckInBetween(LocalDate debut, LocalDate fin);

    // Filtrer par statut ET periode
    List<Reservation> findByStatutAndCheckInBetween(String statut, LocalDate debut, LocalDate fin);
}
