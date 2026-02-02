package com.videoclub;

import java.util.LinkedList;
import java.util.List;

/**
 * Représente un client du vidéoclub.
 * Gère les locations et génère les situations (factures).
 */
public class Client {
    
    private String nom;
    private List<Location> locations = new LinkedList<Location>();
    
    public Client(String nom) {
        this.nom = nom;
    }
    
    public void addLocation(Location location) {
        this.locations.add(location);
    }
    
    public String getNom() {
        return this.nom;
    }
    
    /**
     * Calcule le montant dû pour une location selon le type de film.
     */
    private double calculerMontant(Location location) {
        double montant = 0;
        switch (location.getFilm().getCodePrix()) {
            case Film.NORMAL:
                montant = 2;
                if (location.getNbJours() > 2) 
                    montant += (location.getNbJours() - 2) * 1.5;
                break;
            case Film.NOUVEAUTE:
                montant = location.getNbJours() * 3;
                break;
            case Film.ENFANT:
                montant = 1.5;
                if (location.getNbJours() > 3)
                    montant += (location.getNbJours() - 3) * 1.5;
                break;
            case Film.COFFRET_SERIES_TV:
                montant = location.getNbJours() * 0.5;
                break;
            case Film.CINEPHILE:
                montant = 2;
                if (location.getNbJours() > 1)
                    montant += (location.getNbJours() - 1) * 4;
                break;
        }
        return montant;
    }
    
    /**
     * Calcule les points de fidélité pour une location.
     */
    private int calculerPointsFidelite(Location location) {
        int points = 0;
        switch (location.getFilm().getCodePrix()) {
            case Film.COFFRET_SERIES_TV:
                // Pas de points pour les coffrets séries TV
                break;
            case Film.CINEPHILE:
                // 3 points uniquement si location d'1 jour
                if (location.getNbJours() == 1)
                    points = 3;
                break;
            default:
                // 1 point par défaut
                points = 1;
                // Bonus pour les nouveautés louées au moins 2 jours
                if (location.getFilm().getCodePrix() == Film.NOUVEAUTE && location.getNbJours() > 1)
                    points++;
                break;
        }
        return points;
    }
    
    /**
     * Génère la situation du client au format texte.
     */
    public String situation() {
        double totalDu = 0;
        int pointsFidelites = 0;
        StringBuilder result = new StringBuilder();
        result.append("Situation du client: ").append(getNom()).append("\n");
        
        for (Location location : locations) {
            double montant = calculerMontant(location);
            pointsFidelites += calculerPointsFidelite(location);
            
            result.append("\t").append(location.getFilm().getTitre())
                  .append("\t").append(montant).append("\n");
            totalDu += montant;
        }
        
        result.append("Total du ").append(totalDu).append("\n");
        result.append("Vous gagnez ").append(pointsFidelites).append(" points de fidelite\n");
        
        return result.toString();
    }
    
    /**
     * Génère la situation du client au format HTML.
     */
    public String situationHTML() {
        double totalDu = 0;
        int pointsFidelites = 0;
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"fr\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <title>Situation Client - ").append(getNom()).append("</title>\n");
        html.append("    <style>\n");
        html.append("        body { font-family: Arial, sans-serif; margin: 40px; background-color: #f5f5f5; }\n");
        html.append("        .container { background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); max-width: 600px; margin: auto; }\n");
        html.append("        h1 { color: #2c3e50; border-bottom: 2px solid #3498db; padding-bottom: 10px; }\n");
        html.append("        table { width: 100%; border-collapse: collapse; margin: 20px 0; }\n");
        html.append("        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }\n");
        html.append("        th { background-color: #3498db; color: white; }\n");
        html.append("        tr:hover { background-color: #f1f1f1; }\n");
        html.append("        .total { font-size: 1.2em; font-weight: bold; color: #27ae60; margin-top: 20px; }\n");
        html.append("        .fidelite { color: #e74c3c; font-weight: bold; }\n");
        html.append("        .footer { margin-top: 30px; text-align: center; color: #7f8c8d; font-size: 0.9em; }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <div class=\"container\">\n");
        html.append("        <h1>🎬 Situation du client: ").append(getNom()).append("</h1>\n");
        html.append("        <table>\n");
        html.append("            <tr><th>Film</th><th>Montant (€)</th></tr>\n");
        
        for (Location location : locations) {
            double montant = calculerMontant(location);
            pointsFidelites += calculerPointsFidelite(location);
            
            html.append("            <tr><td>").append(location.getFilm().getTitre())
                .append("</td><td>").append(String.format("%.2f", montant)).append(" €</td></tr>\n");
            totalDu += montant;
        }
        
        html.append("        </table>\n");
        html.append("        <p class=\"total\">💰 Total dû: ").append(String.format("%.2f", totalDu)).append(" €</p>\n");
        html.append("        <p class=\"fidelite\">⭐ Vous gagnez ").append(pointsFidelites).append(" points de fidélité</p>\n");
        html.append("        <div class=\"footer\">Merci de votre fidélité ! - VideoClub</div>\n");
        html.append("    </div>\n");
        html.append("</body>\n");
        html.append("</html>");
        
        return html.toString();
    }
}
