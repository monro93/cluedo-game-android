package com.example.albert.cluedo;

/**
 * Classe que representa un sospitos
 */
public class Sospitos extends Carta {
    private int color;

    /**
     * Constructor amb dos paràmetres
     * @param nom, representa el nom del sospitos
     * @param color, representa el color del sospitos
     */
    public Sospitos(String nom, int color) {
        super(nom);
        this.color = color;
    }

    /**
     * Mètode accessor que obté el color del sospitos
     * @return color
     */
    public int getColor(){
        return color;
    }

}
