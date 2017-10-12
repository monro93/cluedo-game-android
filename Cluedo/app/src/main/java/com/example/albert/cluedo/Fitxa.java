package com.example.albert.cluedo;

/**
 * Classe que representa una fitxa
 */
public class Fitxa {
    protected String nom;
    protected int color;
    protected Casella posicio;
    protected Casella casellaInicial;
    protected Sospitos sospitos;

    /**
     * Constructor amb quatre paràmetres
     * @param nom, representa el nom de la fitxa
     * @param color, representa el color de la fitxa
     * @param casellaInicial, representa la casella on comença la fitxa
     * @param sospitos, representa al sospitos
     */
    public Fitxa(String nom, int color, Casella casellaInicial, Sospitos sospitos){
        this.nom = nom;
        this.color = color;
        this.casellaInicial = casellaInicial;
        this.posicio = casellaInicial;
        this.sospitos = sospitos;
        casellaInicial.colocaFitxaACasella(this);
    }

    /**
     * Mètode que mou les fitxes
     * @param posicio, representa la posició de la fitxa
     */
    public void moure(Casella posicio){
        this.posicio = posicio;
    }

    /**
     * Mètode accessor que obté la posició de la fitxa.
     * @return posicio
     */
    public Casella getPosicio(){
        return posicio;
    }

    /**
     * Mètode accessor que obté el sospitos
     * @return sospitos
     */
    public Sospitos getSospitos(){
        return sospitos;
    }

    public Casella getCasellaInicial(){
        return casellaInicial;
    }
    /**
     * Mètode accessor que obté el nom de la fitxa
     * @return nom
     */
    public String getNom(){
        return nom;
    }
}
