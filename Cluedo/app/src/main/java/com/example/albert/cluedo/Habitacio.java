package com.example.albert.cluedo;

import java.util.ArrayList;
import java.util.Random;

/**
 * Classe que representa una habitació
 */
public class Habitacio extends Carta {
    public ArrayList<Casella> caselles;
    protected ArrayList<Casella> porta;

    /**
     * Constructor amb un paràmetre
     * @param nom, representa el nom de l'habitació
     */
    public Habitacio(String nom) {
        super(nom);
        caselles = new ArrayList<>();
        this.porta = new ArrayList<>();
        //printarNomHabitacio();
        //Log.d("hab:","nom:"+getNom()+"num: "+caselles.size());
    }

    /**
     * Mètode que obté una porta
     * @return porta.get(r.nextInt(porta.size()));
     */
    public Casella getPorta(){
        Random r=new Random();
        return porta.get(r.nextInt(porta.size()));
    }

    /**
     * Mètode accessor que col·loca una porta
     * @param porta, representa una porta de l'habitació
     */
    public void setPorta(Casella porta){
        this.porta.add(porta);
    }

    /**
     * Mètode que afegeix casellas
     * @param casella, representa una casella de l'habitació
     */
    public void afegirCasella(Casella casella){
        caselles.add(casella);
    }

    /**
     * Mètode que afegeix casellas a un arrayList
     * @param caselles, representa una casella de l'habitació
     */
    public void afegirCaselles(ArrayList<Casella> caselles){
        this.caselles = caselles;
    }

    /**
     * Mètode que obté casellas
     * @return caselles
     */
    public ArrayList<Casella> getCaselles(){
        return caselles;
    }

    /**
     * Mètode que obté una casella concreta
     * @param i, representa el número de la casellas que volem
     * @return caselles
     */
    public Casella getCasella(int i){
        return caselles.get(i);
    }

    /**
     * Mètode que diu si la casella esta dintre d'una habitació
     * @param casella, representa una casella d'una habitació
     * @return casella
     */
    public boolean isCasellaInside(Casella casella){
        return caselles.contains(casella);
    }

    /**
     * Mètode que printa el nom de l'habitació
     */
    public void printarNomHabitacio(){
        int posicio = getFilaLlarga();
        int iterador = 0;

        for (Casella casella: caselles){
            if(casella.getPosicioX() == posicio && iterador<nom.length()){
                casella.introduirText(nom.substring(iterador, iterador + 1).toUpperCase());

                iterador++;
            }

        }
    }

    /**
     * Mètode que obté la fila llarga del l'habitació
     * @return posicio+caselles.get(0).getPosicioX()
     */
    private int getFilaLlarga(){
        int fila[] = new int[caselles.size()];
        int i = 0;
        //int UltimaPos = 3;
        int UltimaPos = caselles.get(0).getPosicioX();
        for(Casella casella : caselles){
            if(casella.getPosicioX()==UltimaPos){
                if(casella.tipus == Casella.Tipus.PORTA || casella.tipus == Casella.Tipus.DRAÇERA){
                    fila[i] -= 5;//ponderadament no me interesa aquesta fila
                }else {
                    fila[i]++;
                }
            }else{
                i++;
                UltimaPos = casella.getPosicioX();
                fila[i]++;
            }
        }
        int max = fila[0];
        int posicio = 0;
        for(int j = 0; j < fila.length; j++){
            if(fila[j] > max){
                max = fila[j];
                posicio = j;
            }
        }

        return posicio+caselles.get(0).getPosicioX();
    }

}
