package com.example.albert.cluedo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe que representa un Jugador
 */
public class Jugador  {
    private ArrayList<Sospitos> Sospitos;
    private ArrayList<Habitacio> Habitacio;
    private ArrayList<Arma> Arma;
    private ArrayList<Carta> Tachats;
    private ArrayList<Carta> cartesJugador;
    private Fitxa fitxa;
    private Context context;
    private Carta proximaCartaAMostrar;

    //https://sekthdroid.wordpress.com/2013/01/31/retornar-valor-desde-una-actividad-secundaria-en-android/

    /**
     * Constructor amb dos paràmetres que llegeix XML
     * @param f, representa una fitxa
     * @param context, representa un recurs
     */
    public Jugador(Fitxa f, Context context){
        cartesJugador = new ArrayList<>();
        Tachats = new ArrayList<>();
        fitxa = f;
        this.context = context;
        try{
            Sospitos = LectorXML.llegueixSospitosos(context.getAssets().open("sospitosos.xml"));
            Arma = LectorXML.llegueixArmas(context.getAssets().open("armas.xml"));
            Habitacio = LectorXML.llegueixHabitacions(context.getAssets().open("habitacions.xml"),new ArrayList<Casella>());
        }catch(IOException e){
            Log.e("XML ERROR", "Error IO:" + e.getMessage() + "-" + e.getCause());
        }
    }

    /**
     * Mètode que obté la pròxima carta a mostrar
     * @return Carta;
     */
    public Carta getProximaCartaAMostrar() {
        return proximaCartaAMostrar;
    }

    public ArrayList<Carta> getCartesJugador(){
        return cartesJugador;
    }

    /**
     * Mètode que col·loca la proxima carta a mostrar
     * @param proximaCartaAMostrar
     */
    public void setProximaCartaAMostrar(Carta proximaCartaAMostrar) {
        this.proximaCartaAMostrar = proximaCartaAMostrar;
    }

    /**
     * Mètode que obre el bloc de notes i afegeix els sospitos tatxats a un arrayList
     *
     */
    public void obrirBlocDeNotes(Boolean boleano){
        Intent intent = new Intent(context, Bloc_de_notes.class);
        ArrayList<String> aux = new ArrayList<String>() {
        };
        if (Tachats == null) {
            Tachats = new ArrayList<Carta>();
        }
        for (Carta auxi : Tachats) {
            aux.add(auxi.getNom());
        }
        intent.putStringArrayListExtra("Tachats", aux);
        if(boleano) {
            ((Activity) context).startActivityForResult(intent, Principal.ACTIVITY_BLOC_NOTES);
        }else{
            ((Activity) context).startActivityForResult(intent, Principal.ACTIVITY_BLOC_NOTES_ESCOLLINT);
        }

    }

    /**
     * Mètode que obre el bloc de notas
     */
    public void obrirBlocDeNotes(){
        obrirBlocDeNotes(true);
    }

    /**
     * Mètode que afegeix una carta a les cartes del jugador
     * @param c, representa una carta
     */
    public void afeguirCartaJugador(Carta c){
        cartesJugador.add(c);
        Tachats.add(c);
    }

    /**
     * Mètode que permet saber si es te una carta
     * @param c, representa una carta
     * @return null
     */
    public Boolean bTensCarta(Carta c){
        return cartesJugador.contains(c);
    }

    /**
     * Mètode que busca una carta
     * @param name, representa el nom de la carta
     * @return null
     */
    public Carta BuscarCarta(String name){
        for (Carta carta : Sospitos){
            if(carta.getNom().equals(name)){
                return carta;
            }
        }
        for (Carta carta : Habitacio){
            if(carta.getNom().equals(name)){
                return carta;
            }
        }
        for (Carta carta : Arma){
            if(carta.getNom().equals(name)){
                return carta;
            }
        }
        return null;
    }

    /**
     * Metode que busca una carta d'un jugador
     * @param name, representa el nom de la carta
     * @return null
     */
    public Carta BuscarCartaJugador(String name){
        for (Carta carta : cartesJugador){
            if(carta.getNom().equals(name)){
                return carta;
            }
        }
        return null;
    }

    /**
     * Mètode que actualitza el bloc de notes amb el que ha tatxat el jugador
     * @param CartesTachades
     */
    public void vActualitzaTachats(ArrayList<String> CartesTachades){
        Tachats = null;
        Tachats = new ArrayList<Carta>();
        for(String aux : CartesTachades){
            Tachats.add(BuscarCarta(aux));
        }
    }

    /**
     * Mètode accessor que obté una fitxa
     * @return
     */
    public Fitxa getFitxa(){
        return fitxa;
    }



}
