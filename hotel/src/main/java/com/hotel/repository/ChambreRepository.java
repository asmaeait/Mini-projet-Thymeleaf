package com.hotel.repository;

import com.hotel.model.Chambre;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Repository JPA pour accéder aux chambres dans la base de données
public interface ChambreRepository extends JpaRepository<Chambre, Long> {

    // Récupère les chambres selon leur type (SINGLE, DOUBLE, SUITE)
    List<Chambre> findByType(String type);

    // Récupère les chambres selon leur disponibilité
    List<Chambre> findByDisponible(boolean disponible);

    // Récupère les chambres selon le type ET la disponibilité
    List<Chambre> findByTypeAndDisponible(String type, boolean disponible);
}
