package com.example.albert.cluedo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe que representa una activitat de dialeg
 */
public class DialegFinal extends Activity {
    private TextView tvResultat;
    private LinearLayout LinearCartes;
    protected ArrayList<Sospitos> Sospitos;
    protected ArrayList<Habitacio> Habitacio;
    protected ArrayList<Arma> Arma;

    /**
     * Mètode onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_del_joc);
        tvResultat = (TextView) findViewById(R.id.tvResultat);
        LinearCartes = (LinearLayout) findViewById(R.id.LinearCartes);
        this.setFinishOnTouchOutside(false);
        final Intent intent = getIntent();
        Boolean resultat = intent.getBooleanExtra("Resultat", false);
        ArrayList<String> cartasFinales = intent.getStringArrayListExtra("CartasFinales");

        try{
            Sospitos = LectorXML.llegueixSospitosos(getAssets().open("sospitosos.xml"));
            Arma = LectorXML.llegueixArmas(getAssets().open("armas.xml"));
            Habitacio = LectorXML.llegueixHabitacions(getAssets().open("habitacions.xml"),new ArrayList<Casella>());
        }catch(IOException e){
            Log.e("XML ERROR", "Error IO:" + e.getMessage() + "-" + e.getCause());
        }

        ArrayList<Carta> cartes = new ArrayList<>();
        for (Sospitos aux : Sospitos){
            cartes.add(aux);
        }
        for (Arma aux : Arma){
            cartes.add(aux);
        }
        for (Habitacio aux : Habitacio){
            cartes.add(aux);
        }

        for (Carta aux : cartes){
            for(String auxOriginal : cartasFinales) {
                if(aux.getNom().equals(auxOriginal)){
                    View view = aux.getView(this);
                    LinearCartes.addView(view);
                }
            }
        }

        if (resultat){
            tvResultat.setText("FELICIDADES!\n¡HAS GANADO!");
        }else{
            tvResultat.setText("¡HAS PERDIDO!");
        }
        Button btnSortir = (Button) findViewById(R.id.btnSortir);
        btnSortir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
                setResult(RESULT_OK, intent1);
                //Log.d("He llegado", "He llegado :)");
                finish();
            }
        });

    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }




}
