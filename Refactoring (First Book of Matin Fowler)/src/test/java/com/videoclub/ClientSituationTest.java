package com.videoclub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la méthode situation() de la classe Client.
 * Couvre tous les types de films et les cas limites.
 */
@DisplayName("Tests de la méthode situation()")
class ClientSituationTest {
    
    private Client client;
    
    @BeforeEach
    void setUp() {
        client = new Client("TestClient");
    }
    
    // ==================== Tests Film NORMAL ====================
    
    @Nested
    @DisplayName("Film NORMAL")
    class FilmNormalTests {
        
        @Test
        @DisplayName("Durée <= 2 jours : tarif de base 2€")
        void testDureeInferieureOuEgale2Jours() {
            Film film = new Film("Taxi Driver", Film.NORMAL);
            client.addLocation(new Location(film, 2));
            
            String result = client.situation();
            
            assertTrue(result.contains("Taxi Driver\t2.0"));
            assertTrue(result.contains("Total du 2.0"));
            assertTrue(result.contains("1 points de fidelite"));
        }
        
        @Test
        @DisplayName("Durée > 2 jours : supplément 1.5€/jour")
        void testDureeSuperieure2Jours() {
            Film film = new Film("Taxi Driver", Film.NORMAL);
            client.addLocation(new Location(film, 3));
            
            String result = client.situation();
            
            // 2 + (3-2) * 1.5 = 3.5
            assertTrue(result.contains("Taxi Driver\t3.5"));
            assertTrue(result.contains("Total du 3.5"));
        }
        
        @Test
        @DisplayName("Durée longue (5 jours)")
        void testDureeLongue() {
            Film film = new Film("Film Long", Film.NORMAL);
            client.addLocation(new Location(film, 5));
            
            String result = client.situation();
            
            // 2 + (5-2) * 1.5 = 6.5
            assertTrue(result.contains("Film Long\t6.5"));
        }
        
        @Test
        @DisplayName("1 point de fidélité")
        void testPointsFidelite() {
            Film film = new Film("Film Test", Film.NORMAL);
            client.addLocation(new Location(film, 1));
            
            String result = client.situation();
            
            assertTrue(result.contains("1 points de fidelite"));
        }
    }
    
    // ==================== Tests Film NOUVEAUTE ====================
    
    @Nested
    @DisplayName("Film NOUVEAUTE")
    class FilmNouveauteTests {
        
        @Test
        @DisplayName("1 jour : 3€, 1 point (pas de bonus)")
        void test1Jour_PasDeBonus() {
            Film film = new Film("11 heures 14", Film.NOUVEAUTE);
            client.addLocation(new Location(film, 1));
            
            String result = client.situation();
            
            assertTrue(result.contains("11 heures 14\t3.0"));
            assertTrue(result.contains("1 points de fidelite"));
        }
        
        @Test
        @DisplayName("2 jours : 6€, 2 points (avec bonus)")
        void test2Jours_AvecBonus() {
            Film film = new Film("11 heures 14", Film.NOUVEAUTE);
            client.addLocation(new Location(film, 2));
            
            String result = client.situation();
            
            assertTrue(result.contains("11 heures 14\t6.0"));
            assertTrue(result.contains("2 points de fidelite"));
        }
        
        @Test
        @DisplayName("4 jours : 12€")
        void testPlusieursJours() {
            Film film = new Film("Nouveau Film", Film.NOUVEAUTE);
            client.addLocation(new Location(film, 4));
            
            String result = client.situation();
            
            assertTrue(result.contains("Nouveau Film\t12.0"));
        }
    }
    
    // ==================== Tests Film ENFANT ====================
    
    @Nested
    @DisplayName("Film ENFANT")
    class FilmEnfantTests {
        
        @Test
        @DisplayName("Durée <= 3 jours : tarif de base 1.5€")
        void testDureeInferieureOuEgale3Jours() {
            Film film = new Film("Cendrillon", Film.ENFANT);
            client.addLocation(new Location(film, 3));
            
            String result = client.situation();
            
            assertTrue(result.contains("Cendrillon\t1.5"));
            assertTrue(result.contains("1 points de fidelite"));
        }
        
        @Test
        @DisplayName("Durée > 3 jours : supplément 1.5€/jour")
        void testDureeSuperieure3Jours() {
            Film film = new Film("Cendrillon", Film.ENFANT);
            client.addLocation(new Location(film, 4));
            
            String result = client.situation();
            
            // 1.5 + (4-3) * 1.5 = 3.0
            assertTrue(result.contains("Cendrillon\t3.0"));
        }
        
        @Test
        @DisplayName("Durée longue (6 jours)")
        void testDureeLongue() {
            Film film = new Film("Disney Film", Film.ENFANT);
            client.addLocation(new Location(film, 6));
            
            String result = client.situation();
            
            // 1.5 + (6-3) * 1.5 = 6.0
            assertTrue(result.contains("Disney Film\t6.0"));
        }
    }
    
    // ==================== Tests Film COFFRET-SERIES-TV ====================
    
    @Nested
    @DisplayName("Film COFFRET-SERIES-TV")
    class FilmCoffretSeriesTVTests {
        
        @Test
        @DisplayName("1 jour : 0.50€")
        void test1Jour() {
            Film film = new Film("Breaking Bad", Film.COFFRET_SERIES_TV);
            client.addLocation(new Location(film, 1));
            
            String result = client.situation();
            
            assertTrue(result.contains("Breaking Bad\t0.5"));
        }
        
        @Test
        @DisplayName("7 jours : 3.50€")
        void testPlusieursJours() {
            Film film = new Film("Game of Thrones", Film.COFFRET_SERIES_TV);
            client.addLocation(new Location(film, 7));
            
            String result = client.situation();
            
            // 7 * 0.5 = 3.5
            assertTrue(result.contains("Game of Thrones\t3.5"));
        }
        
        @Test
        @DisplayName("Pas de points de fidélité")
        void testPasDePointsFidelite() {
            Film film = new Film("Série TV", Film.COFFRET_SERIES_TV);
            client.addLocation(new Location(film, 5));
            
            String result = client.situation();
            
            assertTrue(result.contains("0 points de fidelite"));
        }
    }
    
    // ==================== Tests Film CINEPHILE ====================
    
    @Nested
    @DisplayName("Film CINEPHILE")
    class FilmCinephileTests {
        
        @Test
        @DisplayName("1 jour : 2€, 3 points fidélité")
        void test1Jour_AvecBonus() {
            Film film = new Film("Citizen Kane", Film.CINEPHILE);
            client.addLocation(new Location(film, 1));
            
            String result = client.situation();
            
            assertTrue(result.contains("Citizen Kane\t2.0"));
            assertTrue(result.contains("3 points de fidelite"));
        }
        
        @Test
        @DisplayName("2 jours : 6€ (2 + 4), pas de points")
        void test2Jours_SansBonus() {
            Film film = new Film("Citizen Kane", Film.CINEPHILE);
            client.addLocation(new Location(film, 2));
            
            String result = client.situation();
            
            // 2 + (2-1) * 4 = 6
            assertTrue(result.contains("Citizen Kane\t6.0"));
            assertTrue(result.contains("0 points de fidelite"));
        }
        
        @Test
        @DisplayName("3 jours : 10€ (2 + 2*4)")
        void test3Jours() {
            Film film = new Film("Film Rare", Film.CINEPHILE);
            client.addLocation(new Location(film, 3));
            
            String result = client.situation();
            
            // 2 + (3-1) * 4 = 10
            assertTrue(result.contains("Film Rare\t10.0"));
        }
        
        @Test
        @DisplayName("5 jours : 18€ (2 + 4*4)")
        void test5Jours() {
            Film film = new Film("Chef d'Oeuvre", Film.CINEPHILE);
            client.addLocation(new Location(film, 5));
            
            String result = client.situation();
            
            // 2 + (5-1) * 4 = 18
            assertTrue(result.contains("Chef d'Oeuvre\t18.0"));
        }
    }
    
    // ==================== Tests cas limites ====================
    
    @Nested
    @DisplayName("Cas limites")
    class CasLimitesTests {
        
        @Test
        @DisplayName("Client sans location")
        void testClientSansLocation() {
            String result = client.situation();
            
            assertTrue(result.contains("Situation du client: TestClient"));
            assertTrue(result.contains("Total du 0.0"));
            assertTrue(result.contains("0 points de fidelite"));
        }
        
        @Test
        @DisplayName("Nom du client")
        void testNomClient() {
            assertEquals("TestClient", client.getNom());
        }
    }
    
    // ==================== Tests cumul de locations ====================
    
    @Nested
    @DisplayName("Cumul de locations")
    class CumulLocationsTests {
        
        @Test
        @DisplayName("Cumul tous types de films")
        void testCumulTousTypesFilms() {
            client.addLocation(new Location(new Film("Film Normal", Film.NORMAL), 2));      // 2€, 1pt
            client.addLocation(new Location(new Film("Nouveauté", Film.NOUVEAUTE), 1));     // 3€, 1pt
            client.addLocation(new Location(new Film("Film Enfant", Film.ENFANT), 2));      // 1.5€, 1pt
            client.addLocation(new Location(new Film("Série TV", Film.COFFRET_SERIES_TV), 4)); // 2€, 0pt
            client.addLocation(new Location(new Film("Cinéphile", Film.CINEPHILE), 1));     // 2€, 3pt
            
            String result = client.situation();
            
            // Total: 2 + 3 + 1.5 + 2 + 2 = 10.5€
            assertTrue(result.contains("Total du 10.5"));
            // Points: 1 + 1 + 1 + 0 + 3 = 6
            assertTrue(result.contains("6 points de fidelite"));
        }
        
        @Test
        @DisplayName("Cumul avec bonus nouveauté")
        void testCumulAvecBonusNouveaute() {
            client.addLocation(new Location(new Film("Nouveau 1", Film.NOUVEAUTE), 3));  // 9€, 2pts
            client.addLocation(new Location(new Film("Nouveau 2", Film.NOUVEAUTE), 2));  // 6€, 2pts
            
            String result = client.situation();
            
            assertTrue(result.contains("Total du 15.0"));
            assertTrue(result.contains("4 points de fidelite"));
        }
        
        @Test
        @DisplayName("Cumul cinéphile avec et sans bonus")
        void testCumulCinephile() {
            client.addLocation(new Location(new Film("Classique 1", Film.CINEPHILE), 1)); // 2€, 3pts
            client.addLocation(new Location(new Film("Classique 2", Film.CINEPHILE), 2)); // 6€, 0pts
            
            String result = client.situation();
            
            assertTrue(result.contains("Total du 8.0"));
            assertTrue(result.contains("3 points de fidelite"));
        }
    }
    
    // ==================== Tests classes annexes ====================
    
    @Nested
    @DisplayName("Tests classes Film et Location")
    class ClassesAnnexesTests {
        
        @Test
        @DisplayName("Film: getters et setters")
        void testFilmGettersSetters() {
            Film film = new Film("Test Film", Film.NORMAL);
            
            assertEquals("Test Film", film.getTitre());
            assertEquals(Film.NORMAL, film.getCodePrix());
            
            film.setCodePrix(Film.NOUVEAUTE);
            assertEquals(Film.NOUVEAUTE, film.getCodePrix());
        }
        
        @Test
        @DisplayName("Location: getters")
        void testLocationGetters() {
            Film film = new Film("Test Film", Film.NORMAL);
            Location location = new Location(film, 5);
            
            assertEquals(5, location.getNbJours());
            assertEquals(film, location.getFilm());
        }
        
        @Test
        @DisplayName("Constantes Film")
        void testConstantesFilm() {
            assertEquals(0, Film.NORMAL);
            assertEquals(1, Film.NOUVEAUTE);
            assertEquals(2, Film.ENFANT);
            assertEquals(3, Film.COFFRET_SERIES_TV);
            assertEquals(4, Film.CINEPHILE);
        }
    }
}
