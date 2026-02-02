package com.videoclub;

/**
 * Représente une location de film.
 * Associe un film à une durée de location en jours.
 */
public class Location {
    
    private Film unFilm;
    private int nbJours;
    
    public Location(Film unFilm, int nbJours) {
        this.unFilm = unFilm;
        this.nbJours = nbJours;
    }

    public int getNbJours() {
        return this.nbJours;
    }

    public Film getFilm() {
        return this.unFilm;
    }
}
