package com.example.albert.cluedo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Classe que representa una activitat de dialeg per escollir carta
 */
public class DialegEscullCarta extends AppCompatActivity {
    private ArrayList<Carta> cartas;

    /**
     *Mètode onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialeg_escull_carta);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layoutEscullCartes);
        cartas = new ArrayList<>();
        final Intent intent = getIntent();
        String sospitos = intent.getStringExtra("Sospitos");
        String arma = intent.getStringExtra("Arma");
        String habitacio = intent.getStringExtra("Habitacio");

        if(!sospitos.equals("NULL"))
            cartas.add(new Sospitos(sospitos, 0));

        if(!arma.equals("NULL"))
            cartas.add(new Arma(arma));

        if(!habitacio.equals("NULL"))
            cartas.add(new Habitacio(habitacio));

        for(final Carta c: cartas){
            View view = c.getView(this);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.putExtra("carta", c.getNom());
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });
            layout.addView(view);
        }
    }

    /**
     * Mètode que crea un menu
     * @param menu, representa un menú
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dialeg_ecull_carta, menu);
        return true;
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed(){

    }
}
