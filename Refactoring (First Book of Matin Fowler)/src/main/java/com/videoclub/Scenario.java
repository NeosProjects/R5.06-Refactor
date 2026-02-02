package com.videoclub;

/**
 * Classe de démonstration du système de location de films.
 */
public class Scenario {
    
    public static void main(String[] args) {
        System.out.println("=== Démonstration du système de location ===\n");
        
        // Création d'un client avec plusieurs locations
        Client client = new Client("Jean Dupont");
        
        // Ajout de locations de différents types
        client.addLocation(new Location(new Film("Taxi Driver", Film.NORMAL), 3));
        client.addLocation(new Location(new Film("Avatar 3", Film.NOUVEAUTE), 2));
        client.addLocation(new Location(new Film("Cendrillon", Film.ENFANT), 4));
        client.addLocation(new Location(new Film("Breaking Bad Intégrale", Film.COFFRET_SERIES_TV), 7));
        client.addLocation(new Location(new Film("Citizen Kane - Edition Collector", Film.CINEPHILE), 1));
        
        // Affichage de la situation texte
        System.out.println("--- Format TEXTE ---");
        System.out.println(client.situation());
        
        // Affichage de la situation HTML
        System.out.println("\n--- Format HTML ---");
        System.out.println(client.situationHTML());
    }
}
