package com.videoclub;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la méthode situationHTML() de la classe Client.
 * Vérifie la génération correcte du format HTML.
 */
@DisplayName("Tests de la méthode situationHTML()")
class ClientSituationHTMLTest {
    
    private Client client;
    
    @BeforeEach
    void setUp() {
        client = new Client("TestClient");
    }
    
    // ==================== Tests structure HTML ====================
    
    @Nested
    @DisplayName("Structure HTML")
    class StructureHTMLTests {
        
        @Test
        @DisplayName("Contient DOCTYPE et balises HTML de base")
        void testStructureDeBase() {
            String html = client.situationHTML();
            
            assertTrue(html.contains("<!DOCTYPE html>"));
            assertTrue(html.contains("<html lang=\"fr\">"));
            assertTrue(html.contains("</html>"));
            assertTrue(html.contains("<head>"));
            assertTrue(html.contains("</head>"));
            assertTrue(html.contains("<body>"));
            assertTrue(html.contains("</body>"));
        }
        
        @Test
        @DisplayName("Contient le titre avec nom du client")
        void testTitrePage() {
            String html = client.situationHTML();
            
            assertTrue(html.contains("<title>Situation Client - TestClient</title>"));
        }
        
        @Test
        @DisplayName("Contient les styles CSS")
        void testStylesCSS() {
            String html = client.situationHTML();
            
            assertTrue(html.contains("<style>"));
            assertTrue(html.contains("</style>"));
            assertTrue(html.contains("font-family"));
        }
        
        @Test
        @DisplayName("Contient un tableau HTML")
        void testTableau() {
            String html = client.situationHTML();
            
            assertTrue(html.contains("<table>"));
            assertTrue(html.contains("</table>"));
            assertTrue(html.contains("<th>Film</th>"));
            assertTrue(html.contains("<th>Montant"));
        }
        
        @Test
        @DisplayName("Contient le nom du client dans le titre H1")
        void testNomClientDansH1() {
            String html = client.situationHTML();
            
            assertTrue(html.contains("<h1>"));
            assertTrue(html.contains("TestClient"));
        }
    }
    
    // ==================== Tests Film NORMAL en HTML ====================
    
    @Nested
    @DisplayName("Film NORMAL en HTML")
    class FilmNormalHTMLTests {
        
        @Test
        @DisplayName("Affiche le film et son montant")
        void testAffichageFilmNormal() {
            Film film = new Film("Taxi Driver", Film.NORMAL);
            client.addLocation(new Location(film, 2));
            
            String html = client.situationHTML();
            
            assertTrue(html.contains("<td>Taxi Driver</td>"));
            assertTrue(html.contains("2,00 €") || html.contains("2.00 €"));
        }
        
        @Test
        @DisplayName("Calcul correct avec supplément")
        void testCalculAvecSupplement() {
            Film film = new Film("Film Test", Film.NORMAL);
            client.addLocation(new Location(film, 4));
            
            String html = client.situationHTML();
            
            // 2 + (4-2) * 1.5 = 5.0
            assertTrue(html.contains("5,00 €") || html.contains("5.00 €"));
        }
    }
    
    // ==================== Tests Film NOUVEAUTE en HTML ====================
    
    @Nested
    @DisplayName("Film NOUVEAUTE en HTML")
    class FilmNouveauteHTMLTests {
        
        @Test
        @DisplayName("Affiche le montant correct")
        void testAffichageMontant() {
            Film film = new Film("Avatar 3", Film.NOUVEAUTE);
            client.addLocation(new Location(film, 3));
            
            String html = client.situationHTML();
            
            // 3 * 3 = 9€
            assertTrue(html.contains("9,00 €") || html.contains("9.00 €"));
        }
        
        @Test
        @DisplayName("Affiche les points de fidélité avec bonus")
        void testPointsFideliteAvecBonus() {
            Film film = new Film("Nouveau Film", Film.NOUVEAUTE);
            client.addLocation(new Location(film, 2));
            
            String html = client.situationHTML();
            
            assertTrue(html.contains("2 points"));
        }
    }
    
    // ==================== Tests Film ENFANT en HTML ====================
    
    @Nested
    @DisplayName("Film ENFANT en HTML")
    class FilmEnfantHTMLTests {
        
        @Test
        @DisplayName("Tarif de base 1.50€")
        void testTarifDeBase() {
            Film film = new Film("Cendrillon", Film.ENFANT);
            client.addLocation(new Location(film, 2));
            
            String html = client.situationHTML();
            
            assertTrue(html.contains("1,50 €") || html.contains("1.50 €"));
        }
        
        @Test
        @DisplayName("Tarif avec supplément")
        void testTarifAvecSupplement() {
            Film film = new Film("Ratatouille", Film.ENFANT);
            client.addLocation(new Location(film, 5));
            
            String html = client.situationHTML();
            
            // 1.5 + (5-3) * 1.5 = 4.5€
            assertTrue(html.contains("4,50 €") || html.contains("4.50 €"));
        }
    }
    
    // ==================== Tests Film COFFRET-SERIES-TV en HTML ====================
    
    @Nested
    @DisplayName("Film COFFRET-SERIES-TV en HTML")
    class FilmCoffretSeriesTVHTMLTests {
        
        @Test
        @DisplayName("Tarif 0.50€/jour")
        void testTarifParJour() {
            Film film = new Film("Breaking Bad", Film.COFFRET_SERIES_TV);
            client.addLocation(new Location(film, 10));
            
            String html = client.situationHTML();
            
            // 10 * 0.5 = 5€
            assertTrue(html.contains("5,00 €") || html.contains("5.00 €"));
        }
        
        @Test
        @DisplayName("0 points de fidélité")
        void testZeroPointsFidelite() {
            Film film = new Film("Série", Film.COFFRET_SERIES_TV);
            client.addLocation(new Location(film, 5));
            
            String html = client.situationHTML();
            
            assertTrue(html.contains("0 points"));
        }
    }
    
    // ==================== Tests Film CINEPHILE en HTML ====================
    
    @Nested
    @DisplayName("Film CINEPHILE en HTML")
    class FilmCinephileHTMLTests {
        
        @Test
        @DisplayName("1 jour : 2€ et 3 points")
        void test1JourAvecBonus() {
            Film film = new Film("Citizen Kane", Film.CINEPHILE);
            client.addLocation(new Location(film, 1));
            
            String html = client.situationHTML();
            
            assertTrue(html.contains("2,00 €") || html.contains("2.00 €"));
            assertTrue(html.contains("3 points"));
        }
        
        @Test
        @DisplayName("Délai dépassé : tarif majoré, 0 points")
        void testDelaiDepasseSansBonus() {
            Film film = new Film("Film Rare", Film.CINEPHILE);
            client.addLocation(new Location(film, 3));
            
            String html = client.situationHTML();
            
            // 2 + (3-1) * 4 = 10€
            assertTrue(html.contains("10,00 €") || html.contains("10.00 €"));
            assertTrue(html.contains("0 points"));
        }
    }
    
    // ==================== Tests totaux et récapitulatif ====================
    
    @Nested
    @DisplayName("Totaux et récapitulatif")
    class TotauxRecapitulatifTests {
        
        @Test
        @DisplayName("Client sans location : total 0€")
        void testClientSansLocation() {
            String html = client.situationHTML();
            
            assertTrue(html.contains("0,00 €") || html.contains("0.00 €"));
            assertTrue(html.contains("0 points"));
        }
        
        @Test
        @DisplayName("Cumul de plusieurs locations")
        void testCumulLocations() {
            client.addLocation(new Location(new Film("Film 1", Film.NORMAL), 2));     // 2€
            client.addLocation(new Location(new Film("Film 2", Film.ENFANT), 3));     // 1.5€
            client.addLocation(new Location(new Film("Film 3", Film.COFFRET_SERIES_TV), 2)); // 1€
            
            String html = client.situationHTML();
            
            // Total: 2 + 1.5 + 1 = 4.5€
            assertTrue(html.contains("4,50 €") || html.contains("4.50 €"));
            // Points: 1 + 1 + 0 = 2
            assertTrue(html.contains("2 points"));
        }
        
        @Test
        @DisplayName("Tous les types de films")
        void testTousTypesFilms() {
            client.addLocation(new Location(new Film("Normal", Film.NORMAL), 1));
            client.addLocation(new Location(new Film("Nouveau", Film.NOUVEAUTE), 1));
            client.addLocation(new Location(new Film("Enfant", Film.ENFANT), 1));
            client.addLocation(new Location(new Film("Série", Film.COFFRET_SERIES_TV), 1));
            client.addLocation(new Location(new Film("Ciné", Film.CINEPHILE), 1));
            
            String html = client.situationHTML();
            
            // Vérifie que tous les films apparaissent
            assertTrue(html.contains("<td>Normal</td>"));
            assertTrue(html.contains("<td>Nouveau</td>"));
            assertTrue(html.contains("<td>Enfant</td>"));
            assertTrue(html.contains("<td>Série</td>"));
            assertTrue(html.contains("<td>Ciné</td>"));
        }
    }
    
    // ==================== Tests format HTML valide ====================
    
    @Nested
    @DisplayName("Validité HTML")
    class ValiditeHTMLTests {
        
        @Test
        @DisplayName("Encodage UTF-8 déclaré")
        void testEncodageUTF8() {
            String html = client.situationHTML();
            
            assertTrue(html.contains("charset=\"UTF-8\""));
        }
        
        @Test
        @DisplayName("Contient le footer")
        void testFooter() {
            String html = client.situationHTML();
            
            assertTrue(html.contains("class=\"footer\""));
            assertTrue(html.contains("VideoClub"));
        }
        
        @Test
        @DisplayName("Classes CSS présentes")
        void testClassesCSS() {
            String html = client.situationHTML();
            
            assertTrue(html.contains("class=\"container\""));
            assertTrue(html.contains("class=\"total\""));
            assertTrue(html.contains("class=\"fidelite\""));
        }
    }
}
