package com.videoclub;

/**
 * Représente un film disponible à la location.
 * Chaque film a un titre et une catégorie de prix.
 */
public class Film {
    
    // Catégories de films
    public static final int NORMAL = 0;
    public static final int NOUVEAUTE = 1;
    public static final int ENFANT = 2;
    public static final int COFFRET_SERIES_TV = 3;
    public static final int CINEPHILE = 4;
    
    private String titre;
    private int codePrix;
    
    public Film(String titre, int codePrix) {
        this.titre = titre;
        this.codePrix = codePrix;
    }

    public String getTitre() {
        return this.titre;
    }

    public void setCodePrix(int codePrix) {
        this.codePrix = codePrix;
    }

    public int getCodePrix() {
        return this.codePrix;
    }
}
