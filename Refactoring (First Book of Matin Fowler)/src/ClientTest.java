import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ClientTest {
    
    private Client client;
    
    @Before
    public void setUp() {
        client = new Client("TestClient");
    }
    
    // ==================== Tests Film NORMAL ====================
    
    @Test
    public void testFilmNormal_DureeInferieureOuEgale2Jours() {
        Film film = new Film("Taxi Driver", Film.NORMAL);
        Location location = new Location(film, 2);
        client.addLocation(location);
        
        String result = client.situation();
        
        assertTrue(result.contains("Taxi Driver\t2.0"));
        assertTrue(result.contains("Total du 2.0"));
        assertTrue(result.contains("1 points de fidelite"));
    }
    
    @Test
    public void testFilmNormal_DureeSuperieure2Jours() {
        Film film = new Film("Taxi Driver", Film.NORMAL);
        Location location = new Location(film, 3);
        client.addLocation(location);
        
        String result = client.situation();
        
        // 2 + (3-2) * 1.5 = 3.5
        assertTrue(result.contains("Taxi Driver\t3.5"));
        assertTrue(result.contains("Total du 3.5"));
        assertTrue(result.contains("1 points de fidelite"));
    }
    
    @Test
    public void testFilmNormal_DureeLongue() {
        Film film = new Film("Film Long", Film.NORMAL);
        Location location = new Location(film, 5);
        client.addLocation(location);
        
        String result = client.situation();
        
        // 2 + (5-2) * 1.5 = 2 + 4.5 = 6.5
        assertTrue(result.contains("Film Long\t6.5"));
        assertTrue(result.contains("Total du 6.5"));
    }
    
    // ==================== Tests Film NOUVEAUTE ====================
    
    @Test
    public void testFilmNouveaute_1Jour_PasDeBonus() {
        Film film = new Film("11 heures 14", Film.NOUVEAUTE);
        Location location = new Location(film, 1);
        client.addLocation(location);
        
        String result = client.situation();
        
        // 1 * 3 = 3
        assertTrue(result.contains("11 heures 14\t3.0"));
        assertTrue(result.contains("Total du 3.0"));
        assertTrue(result.contains("1 points de fidelite")); // Pas de bonus
    }
    
    @Test
    public void testFilmNouveaute_2Jours_AvecBonus() {
        Film film = new Film("11 heures 14", Film.NOUVEAUTE);
        Location location = new Location(film, 2);
        client.addLocation(location);
        
        String result = client.situation();
        
        // 2 * 3 = 6
        assertTrue(result.contains("11 heures 14\t6.0"));
        assertTrue(result.contains("Total du 6.0"));
        assertTrue(result.contains("2 points de fidelite")); // 1 + 1 bonus
    }
    
    @Test
    public void testFilmNouveaute_PlusieursJours_AvecBonus() {
        Film film = new Film("Nouveau Film", Film.NOUVEAUTE);
        Location location = new Location(film, 4);
        client.addLocation(location);
        
        String result = client.situation();
        
        // 4 * 3 = 12
        assertTrue(result.contains("Nouveau Film\t12.0"));
        assertTrue(result.contains("Total du 12.0"));
        assertTrue(result.contains("2 points de fidelite")); // 1 + 1 bonus
    }
    
    // ==================== Tests Film ENFANT ====================
    
    @Test
    public void testFilmEnfant_DureeInferieureOuEgale3Jours() {
        Film film = new Film("Cendrillon", Film.ENFANT);
        Location location = new Location(film, 3);
        client.addLocation(location);
        
        String result = client.situation();
        
        // 1.5 (tarif de base)
        assertTrue(result.contains("Cendrillon\t1.5"));
        assertTrue(result.contains("Total du 1.5"));
        assertTrue(result.contains("1 points de fidelite"));
    }
    
    @Test
    public void testFilmEnfant_DureeSuperieure3Jours() {
        Film film = new Film("Cendrillon", Film.ENFANT);
        Location location = new Location(film, 4);
        client.addLocation(location);
        
        String result = client.situation();
        
        // 1.5 + (4-3) * 1.5 = 3.0
        assertTrue(result.contains("Cendrillon\t3.0"));
        assertTrue(result.contains("Total du 3.0"));
        assertTrue(result.contains("1 points de fidelite"));
    }
    
    @Test
    public void testFilmEnfant_DureeLongue() {
        Film film = new Film("Disney Film", Film.ENFANT);
        Location location = new Location(film, 6);
        client.addLocation(location);
        
        String result = client.situation();
        
        // 1.5 + (6-3) * 1.5 = 1.5 + 4.5 = 6.0
        assertTrue(result.contains("Disney Film\t6.0"));
        assertTrue(result.contains("Total du 6.0"));
    }
    
    // ==================== Tests cas limites ====================
    
    @Test
    public void testClientSansLocation() {
        String result = client.situation();
        
        assertTrue(result.contains("Situation du client: TestClient"));
        assertTrue(result.contains("Total du 0.0"));
        assertTrue(result.contains("0 points de fidelite"));
    }
    
    @Test
    public void testNomClient() {
        assertEquals("TestClient", client.getNom());
    }
    
    @Test
    public void testFilmNormal_ExactementDeuxJours() {
        Film film = new Film("Film Test", Film.NORMAL);
        Location location = new Location(film, 2);
        client.addLocation(location);
        
        String result = client.situation();
        
        // Exactement 2 jours = pas de supplément
        assertTrue(result.contains("Film Test\t2.0"));
    }
    
    @Test
    public void testFilmEnfant_ExactementTroisJours() {
        Film film = new Film("Film Enfant", Film.ENFANT);
        Location location = new Location(film, 3);
        client.addLocation(location);
        
        String result = client.situation();
        
        // Exactement 3 jours = pas de supplément
        assertTrue(result.contains("Film Enfant\t1.5"));
    }
    
    // ==================== Tests cumul de locations ====================
    
    @Test
    public void testCumulLocations_TousTypesFilms() {
        Film filmNormal = new Film("Taxi Driver", Film.NORMAL);
        Film filmNouveaute = new Film("11 heures 14", Film.NOUVEAUTE);
        Film filmEnfant = new Film("Cendrillon", Film.ENFANT);
        
        client.addLocation(new Location(filmNormal, 2));      // 2.0€
        client.addLocation(new Location(filmNouveaute, 1));   // 3.0€
        client.addLocation(new Location(filmEnfant, 2));      // 1.5€
        
        String result = client.situation();
        
        assertTrue(result.contains("Taxi Driver\t2.0"));
        assertTrue(result.contains("11 heures 14\t3.0"));
        assertTrue(result.contains("Cendrillon\t1.5"));
        assertTrue(result.contains("Total du 6.5")); // 2 + 3 + 1.5
        assertTrue(result.contains("3 points de fidelite")); // 1 + 1 + 1 (pas de bonus nouveauté car 1 jour)
    }
    
    @Test
    public void testCumulLocations_AvecBonusFidelite() {
        Film filmNouveaute1 = new Film("Nouveau 1", Film.NOUVEAUTE);
        Film filmNouveaute2 = new Film("Nouveau 2", Film.NOUVEAUTE);
        
        client.addLocation(new Location(filmNouveaute1, 3));  // 9€, 2 points
        client.addLocation(new Location(filmNouveaute2, 2));  // 6€, 2 points
        
        String result = client.situation();
        
        assertTrue(result.contains("Total du 15.0"));
        assertTrue(result.contains("4 points de fidelite")); // 2 + 2 (bonus pour chaque nouveauté >= 2 jours)
    }
    
    @Test
    public void testCumulLocations_MixAvecEtSansBonus() {
        Film filmNouveaute = new Film("Nouveau", Film.NOUVEAUTE);
        Film filmNormal = new Film("Normal", Film.NORMAL);
        
        client.addLocation(new Location(filmNouveaute, 2));  // 6€, 2 points (avec bonus)
        client.addLocation(new Location(filmNormal, 5));     // 6.5€, 1 point
        
        String result = client.situation();
        
        assertTrue(result.contains("Total du 12.5"));
        assertTrue(result.contains("3 points de fidelite")); // 2 + 1
    }
    
    // ==================== Tests classes Film et Location ====================
    
    @Test
    public void testFilmGetters() {
        Film film = new Film("Test Film", Film.NORMAL);
        
        assertEquals("Test Film", film.getTitre());
        assertEquals(Film.NORMAL, film.getCodePrix());
    }
    
    @Test
    public void testFilmSetCodePrix() {
        Film film = new Film("Test Film", Film.NORMAL);
        film.setCodePrix(Film.NOUVEAUTE);
        
        assertEquals(Film.NOUVEAUTE, film.getCodePrix());
    }
    
    @Test
    public void testLocationGetters() {
        Film film = new Film("Test Film", Film.NORMAL);
        Location location = new Location(film, 5);
        
        assertEquals(5, location.getNbJours());
        assertEquals(film, location.getFilm());
    }
    
    @Test
    public void testConstantesFilm() {
        assertEquals(0, Film.NORMAL);
        assertEquals(1, Film.NOUVEAUTE);
        assertEquals(2, Film.ENFANT);
    }
}
