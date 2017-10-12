package com.example.albert.cluedo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe que representa una activitat bloc de notas
 */
public class Bloc_de_notes extends AppCompatActivity {
    protected ArrayList<Sospitos> Sospitos;
    protected ArrayList<Habitacio> Habitacio;
    protected ArrayList<Arma> Arma;
    protected ArrayList<Carta> CartesMarcades;
    LinearLayout Bloc_notes_layout;
    protected ArrayList<String> CartesMarcaAntiga;

    /**
     * Mètode onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CartesMarcades = new ArrayList<>();
        setContentView(R.layout.bloc_notes);
        Bloc_notes_layout=(LinearLayout) findViewById(R.id.Bloc_notes_layout);

        try{
            Sospitos = LectorXML.llegueixSospitosos(getAssets().open("sospitosos.xml"));
            Arma = LectorXML.llegueixArmas(getAssets().open("armas.xml"));
            Habitacio = LectorXML.llegueixHabitacions(getAssets().open("habitacions.xml"),new ArrayList<Casella>());
        }catch(IOException e){
            Log.e("XML ERROR", "Error IO:" + e.getMessage() + "-" + e.getCause());
        }

        CartesMarcaAntiga = new ArrayList<>();
        Intent intent = getIntent();
        CartesMarcaAntiga = intent.getStringArrayListExtra("Tachats");
/*
        for(String nom : CartesMarcaAntiga){
            CartesMarcades.add(BuscarCarta(nom));
        }
*/
        vCreate();

    }

    /**
     * Constructor per defecte
     */
    public Bloc_de_notes(){

    }

    /**
     * Mètode que crea el bloc de notas
     */
    public void vCreate(){


        Bloc_notes_layout.addView(vTextView("Sospechosos"));

        for(Carta aux : Sospitos){
            Bloc_notes_layout.addView(new LinearLayoutPersonalitzat(this,aux.getNom()));
        }

        Bloc_notes_layout.addView(vTextView("Armas"));

        for(Carta aux : Arma){
            Bloc_notes_layout.addView(new LinearLayoutPersonalitzat(this,aux.getNom()));
        }

        Bloc_notes_layout.addView(vTextView("Habitaciones"));

        for(Carta aux : Habitacio){
            Bloc_notes_layout.addView(new LinearLayoutPersonalitzat(this,aux.getNom()));
        }
    }

    /**
     * Mètode que en tornar endarrera guarda els sospitosos tatxats a jugador
     */
    @Override
    public void onBackPressed(){
        Intent resultData = new Intent();
        Log.d("Antes de de salir: ", CartesMarcades.toString());
        ArrayList<String> cartasMarcadas = new ArrayList<>();
        for(Carta aux : CartesMarcades){
            cartasMarcadas.add(aux.getNom());
        }
        Log.d("Antes de salir: ", CartesMarcades.toString());
        resultData.putStringArrayListExtra("Marcats", cartasMarcadas);
        setResult(RESULT_OK, resultData);
        Log.d("He llegado", "He llegado :)");
        //  finish();
        super.onBackPressed();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event)  {

        if (keyCode == KeyEvent.KEYCODE_BACK)  //Override Keyback to do nothing in this case.
        {
            return true;
        }
        return super.onKeyDown(keyCode, event);  //-->All others key will work as usual
    }

    /**
     * Mètode que afegeix textview al bloc de notas
     * @param Text
     * @return textView
     */
    private TextView vTextView(String Text){
        TextView textView = new TextView(this);
        textView.setText(Text);
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(20);
        return textView;
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
     * Classe interna que crea un linear layout personalitzat
     */
    public class LinearLayoutPersonalitzat extends LinearLayout{
        protected TextView Text;
        protected CheckBox cBox;
        public LinearLayoutPersonalitzat(Context context, final String Texto){
            super(context);
            LinearLayout.LayoutParams ladderParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, Gravity.CENTER);
            LinearLayout Cartes = new LinearLayout(context);
            Cartes.setOrientation(HORIZONTAL);
            Cartes.setLayoutParams(ladderParams);
            Text = new TextView(context);
            cBox = new CheckBox(context);
            Text.setText(Texto);

            cBox.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL | Gravity.CENTER);

            Text.setTextSize(25);
            Text.setTextColor(Color.BLACK);

            Cartes.addView(cBox);
            Cartes.addView(Text);

            GradientDrawable shape =  new GradientDrawable();
            shape.setCornerRadius(30);
            shape.setStroke(1, Color.BLACK);
            shape.setShape(GradientDrawable.RECTANGLE);
            Cartes.setBackground(shape);
            cBox.setEnabled(true);
            cBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                /**
                 * Mètode que canvia el color del sospitos en tatxar-lo
                 * @param buttonView, representa la view del botó
                 * @param isChecked, diu si esta clicat el checkbox
                 */
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        //  cBox.setChecked(false);
                        Text.setTextColor(Color.GRAY);
                        Log.d("a", "" + Text.getText().toString());
                        CartesMarcades.add(BuscarCarta(Text.getText().toString()));
                        Log.d("a", "" + CartesMarcades.toString());
                    } else {
                        //   cBox.setChecked(true);
                        Text.setTextColor(Color.BLACK);
                        //while(BuscarCarta(Text.getText().toString())!=null) {
                        //do {
                        CartesMarcades.remove(BuscarCarta(Text.getText().toString()));
                        //}while(BuscarCarta(Text.getText().toString()) != null);
                        //}
                        Log.d("Buscador de carta: ", BuscarCarta(Text.getText().toString()).toString());
                        Log.d("a", "" + CartesMarcades.toString());

                    }
                }
            });
            Cartes.setOnClickListener(new OnClickListener() {
                /**
                 * Mètode que clica o desclica el checkbox
                 * @param v, representa una view
                 */
                @Override
                public void onClick(View v) {
                    if (cBox.isChecked()) {
                        cBox.setChecked(false);
                    } else {
                        cBox.setChecked(true);
                    }
                }
            });

            this.addView(Cartes);
            if(CartesMarcaAntiga.contains(Text.getText())){
                cBox.setChecked(true);
            }
        }
    }

    /**
     * Mètode que agafa les opcions del menú i indica que fa cadascuna
     * @param item, representa un element del menú
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
