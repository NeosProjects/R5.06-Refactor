# Système de Location de Films - Documentation

## 1. Fonctionnalité principale

Ce code implémente un **système de gestion de locations de films** pour un vidéoclub. Il permet de :
- Gérer des clients et leurs locations de films
- Calculer le montant dû pour chaque location selon le type de film
- Accumuler des points de fidélité
- Générer une situation récapitulative pour un client

---

## 2. Règles métiers

### Tarification selon le type de film :

| Type de Film | Tarif de base | Supplément |
|--------------|---------------|------------|
| **NORMAL** | 2€ | +1.5€/jour au-delà de 2 jours |
| **NOUVEAUTE** | 3€/jour | - |
| **ENFANT** | 1.5€ | +1.5€/jour au-delà de 3 jours |

### Points de fidélité :
- **1 point** par location (quel que soit le type)
- **+1 point bonus** pour les nouveautés louées au moins 2 jours

---

## 3. Diagramme de classes

```mermaid
classDiagram
    class Client {
        -String nom
        -List~Location~ locations
        +Client(String nom)
        +addLocation(Location location) void
        +getNom() String
        +situation() String
    }
    
    class Location {
        -Film unFilm
        -int nbJours
        +Location(Film unFilm, int nbJours)
        +getNbJours() int
        +getFilm() Film
    }
    
    class Film {
        -String titre
        -int codePrix
        +ENFANT: int = 2$
        +NOUVEAUTE: int = 1$
        +NORMAL: int = 0$
        +Film(String titre, int codePrix)
        +getTitre() String
        +setCodePrix(int codePrix) void
        +getCodePrix() int
    }
    
    Client "1" o-- "0..*" Location : locations
    Location "1" --> "1" Film : unFilm
```

**Multiplicités :**
- Un `Client` possède **0 à plusieurs** `Location` (agrégation)
- Une `Location` est associée à **exactement 1** `Film`

---

## 4. Diagramme de séquences

```mermaid
sequenceDiagram
    participant Main
    participant Client
    participant Location
    participant Film
    
    Main->>Client: situation()
    activate Client
    
    Client->>Client: getNom()
    
    loop Pour chaque Location
        Client->>Location: getFilm()
        activate Location
        Location-->>Client: Film
        deactivate Location
        
        Client->>Film: getCodePrix()
        activate Film
        Film-->>Client: codePrix
        deactivate Film
        
        Client->>Location: getNbJours()
        activate Location
        Location-->>Client: nbJours
        deactivate Location
        
        Note over Client: Calcul du montant selon codePrix et nbJours
        
        Client->>Film: getCodePrix()
        activate Film
        Film-->>Client: codePrix
        deactivate Film
        
        Note over Client: Calcul des points fidélité
        
        Client->>Film: getTitre()
        activate Film
        Film-->>Client: titre
        deactivate Film
        
        Note over Client: Mise en forme de la ligne
    end
    
    Note over Client: Ajout du récapitulatif (total + points)
    
    Client-->>Main: String result
    deactivate Client
```

---

## 5. Tests unitaires

Les tests JUnit sont disponibles dans `src/ClientTest.java`. Ils couvrent :

- ✅ Film NORMAL (≤2 jours et >2 jours)
- ✅ Film NOUVEAUTE (1 jour et ≥2 jours avec bonus fidélité)
- ✅ Film ENFANT (≤3 jours et >3 jours)
- ✅ Client sans location
- ✅ Cumul de plusieurs locations
- ✅ Calcul correct des points de fidélité

### Exécution des tests

```bash
# Compilation
javac -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar src/*.java

# Exécution
java -cp .:junit-4.13.2.jar:hamcrest-core-1.3.jar org.junit.runner.JUnitCore ClientTest
```

---

## Structure du projet

```
src/
├── Client.java      # Classe principale gérant les locations
├── Film.java        # Représente un film avec son type/prix
├── Location.java    # Association entre un film et une durée
├── Scenario.java    # Tests manuels (legacy)
└── ClientTest.java  # Tests JUnit automatisés
```
