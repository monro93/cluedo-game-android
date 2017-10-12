package com.example.albert.cluedo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Classe que representa una activitat de dialeg principal
 */
public class DialegPrincipal extends Activity{
    Button btnDau, btnPassadis, btnTorn, btnNotes;
    TextView tvJugador;

    Dau dau;

    /**
     * Mètode que obre les opcions del dialeg en tornar enrera
     */
    @Override
    public void onBackPressed() {}
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialeg_principal);
        this.setFinishOnTouchOutside(false);

        final Dau dau = new Dau();
        Intent intent = getIntent();
        Boolean Dau_tirat = intent.getBooleanExtra("DauTirat", false);
        Boolean SaltEspecial = intent.getBooleanExtra("SaltEspecial", false);
        String nom = intent.getStringExtra("Name");

        btnDau = (Button)findViewById(R.id.btnDau);
        btnPassadis = (Button)findViewById(R.id.btnPassadis);
        btnNotes = (Button)findViewById(R.id.btnNotes);
        btnTorn = (Button)findViewById(R.id.btnTorn);
        tvJugador = (TextView) findViewById(R.id.jugador);
        tvJugador.setText(nom);

        btnDau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dau.mostraDau(getBaseContext());

                Intent resultData = new Intent();
                resultData.putExtra("Opcio", "Dau");
                setResult(RESULT_OK, resultData);
                //Log.d("He llegado", "He llegado :)");
                finish();
            }
        });

        btnNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultData = new Intent();
                resultData.putExtra("Opcio", "BlocNotes");
                setResult(RESULT_OK, resultData);
                //Log.d("He llegado", "He llegado :)");
                finish();
            }
        });

        btnTorn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent resultData = new Intent();
                resultData.putExtra("Opcio", "PassarTorn");
                setResult(RESULT_OK, resultData);
                //Log.d("He llegado", "He llegado :)");
                finish();
            }
        });

        if(Dau_tirat){
            btnDau.setVisibility(View.GONE);
            btnTorn.setEnabled(true);

        }else {
            if(SaltEspecial){
                btnPassadis.setVisibility(View.VISIBLE);
                btnPassadis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent resultData = new Intent();
                        resultData.putExtra("Opcio", "PasadisSecret");
                        setResult(RESULT_OK, resultData);
                        //Log.d("He llegado", "He llegado :)");

                        finish();
                    }
                });

            }
            btnTorn.setEnabled(false);
        }


        /*btnPassadis.setOnClickListener(this);
        btnNotes.setOnClickListener(this);
        btnTorn.setOnClickListener(this);*/


    }



}
