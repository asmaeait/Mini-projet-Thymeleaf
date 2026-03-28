#  Hotel Royal — Système de Réservation

Projet académique réalisé dans le cadre du cours **Développement Jakarta EE : Spring**.  
Application web de gestion hôtelière construite avec **Spring Boot** et **Thymeleaf**.

---

##  Description

Hotel Royal est un système de réservation d'hôtel qui permet de gérer les chambres, les clients et les séjours.  
L'application offre un CRUD complet, des filtres avancés et un tableau de bord statistique.

---

##  Fonctionnalités

### Gestion des Chambres
- Ajouter, modifier, supprimer une chambre
- Chaque chambre possède : un numéro, un étage, un type, un prix par nuit et une disponibilité
- Filtrer par **type** (Single / Double / Suite) et par **disponibilité**
- La disponibilité se met à jour automatiquement lors d'une réservation confirmée

### Gestion des Clients
- Ajouter, modifier, supprimer un client
- Chaque client possède : un nom et un numéro de passeport

### Gestion des Réservations
- Créer une réservation en choisissant un client et une chambre
- **Calcul automatique du coût total** : nombre de nuits × prix par nuit
- Statuts disponibles : `EN_ATTENTE`, `CONFIRMEE`, `ANNULEE`
- Filtrer par **statut** et par **période** (date de début / date de fin)

### Statistiques
- **Taux d'occupation** avec barre de progression
- **Revenu total** des réservations confirmées
- **RevPAR** (Revenue Per Available Room) par type de chambre

---

##  Structure du projet

```
src/
└── main/
    ├── java/com/hotel/
    │   ├── HotelApplication.java        # Point d'entrée Spring Boot
    │   ├── DataInitializer.java         # Données de test insérées au démarrage
    │   ├── model/
    │   │   ├── Chambre.java             # Entité chambre
    │   │   ├── Client.java              # Entité client
    │   │   └── Reservation.java         # Entité réservation (@ManyToOne)
    │   ├── repository/
    │   │   ├── ChambreRepository.java   # Requêtes JPA pour les chambres
    │   │   ├── ClientRepository.java    # Requêtes JPA pour les clients
    │   │   └── ReservationRepository.java
    │   └── controller/
    │       ├── HomeController.java      # Page d'accueil
    │       ├── ChambreController.java   # CRUD chambres
    │       ├── ClientController.java    # CRUD clients
    │       ├── ReservationController.java
    │       └── StatController.java      # Statistiques
    └── resources/
        ├── application.properties
        └── templates/
            ├── index.html
            ├── chambres/
            │   ├── liste.html
            │   └── form.html
            ├── clients/
            │   ├── liste.html
            │   └── form.html
            └── reservations/
                ├── liste.html
                ├── form.html
                └── stats.html
```

---

##  Modèle de données

### Entités

| Entité | Attributs |
|--------|-----------|
| `Chambre` | id, numero, etage, type, prixNuit, disponible |
| `Client` | id, nom, passeport |
| `Reservation` | id, checkIn, checkOut, statut, total, chambre, client |

### Relations

```
Reservation ──── @ManyToOne ──── Chambre
Reservation ──── @ManyToOne ──── Client
```

---

##  Technologies utilisées

| Technologie | Rôle |
|-------------|------|
| Java 17 | Langage principal |
| Spring Boot 3.2 | Framework backend |
| Spring Data JPA | Accès base de données |
| Thymeleaf | Moteur de templates HTML |
| H2 Database | Base de données en mémoire |
| Lombok | Génération automatique du code (getters, setters…) |
| Maven | Gestionnaire de dépendances |

---

##  Lancer le projet

### Prérequis
- Java 17 ou supérieur
- IntelliJ IDEA (recommandé)
- Maven (inclus dans IntelliJ)

### Étapes

**1. Cloner le dépôt**
```bash
git clone https://github.com/ton-username/hotel-royal.git
cd hotel-royal
```

**2. Ouvrir dans IntelliJ IDEA**
```
File → Open → sélectionner le dossier hotel-royal → Trust Project
```

**3. Activer Lombok**
```
File → Settings → Build → Compiler → Annotation Processors → Enable annotation processing
```

**4. Lancer l'application**

Ouvrir `HotelApplication.java` et cliquer sur le bouton ▶

**5. Ouvrir le navigateur**
```
http://localhost:8080
```

---

##  Pages disponibles

| URL | Description |
|-----|-------------|
| `http://localhost:8080/` | Page d'accueil |
| `http://localhost:8080/chambres` | Liste des chambres |
| `http://localhost:8080/clients` | Liste des clients |
| `http://localhost:8080/reservations` | Liste des réservations |
| `http://localhost:8080/stats` | Tableau de bord statistique |
| `http://localhost:8080/h2-console` | Console base de données H2 |



---

##  Données de test

Au démarrage, l'application insère automatiquement :

- **6 chambres** : 2 Single (étage 1), 2 Double (étage 2), 2 Suite (étage 3)
- **4 clients** : Karim Benali, Sara Mansouri, Youssef Alaoui, Nadia Cherif
- **4 réservations** : 2 confirmées, 1 en attente, 1 annulée

---

##  Notes techniques

- La base de données H2 est **en mémoire** : les données sont réinitialisées à chaque redémarrage
- Le calcul du total est automatique : `nombre de nuits × prix par nuit`
- Quand une réservation est confirmée, la chambre passe automatiquement en **non disponible**
- Quand une réservation est supprimée, la chambre redevient **disponible**

---

## Demo
https://github.com/user-attachments/assets/a55a00d4-c7e1-48d8-a7dc-927279f37e30

