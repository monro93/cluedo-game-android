package com.example.albert.cluedo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Classe que representa una activitat e dialeg per escollir les fitxes
 */
public class DialegFitxes extends Activity{

    private RadioButton rb1, rb2 , rb3 , rb4, rb5, rb6;
    private RadioGroup radiogroup1, radiogroup2;
    private Button btncheck;
    private Boolean bRG1Active = false;
    private Boolean bRG2Active = false;
    private static int fitxaUsada = -1;

    /**
     * Mètode onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialeg_fixes);
        this.setFinishOnTouchOutside(false);
        rb1 = (RadioButton) findViewById(R.id.radio0);
        rb2 = (RadioButton) findViewById(R.id.radio01);
        rb3 = (RadioButton) findViewById(R.id.radio02);
        rb4 = (RadioButton) findViewById(R.id.radio03);
        rb5 = (RadioButton) findViewById(R.id.radio04);
        rb6 = (RadioButton) findViewById(R.id.radio05);
        btncheck = (Button) findViewById(R.id.btncheck);
        radiogroup1 = (RadioGroup) findViewById(R.id.radiogroup1);
        radiogroup2 = (RadioGroup) findViewById(R.id.radiogroup2);
        radiogroup1.clearCheck();
        radiogroup2.clearCheck();
        btncheck.setEnabled(false);
        RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(!bRG2Active) {
                    if(checkedId != -1){
                        bRG1Active = true;
                        radiogroup2.clearCheck();
                        btncheck.setEnabled(true);
                    }
                }
                bRG2Active = false;

            }
        };
        RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(!bRG1Active) {
                    if(checkedId != -1){
                        bRG2Active = true;
                        radiogroup1.clearCheck();
                        btncheck.setEnabled(true);
                    }
                }
                bRG1Active = false;

            }
        };
        radiogroup1.setOnCheckedChangeListener(listener1);
        radiogroup2.setOnCheckedChangeListener(listener2);

        if(fitxaUsada!=-1){
            ((TextView) findViewById(R.id.txtViewJugFitxa)).setText("Jugador 2 seleciona una ficha");
            ((RadioButton) findViewById(fitxaUsada)).setEnabled(false);
        }
        Log.d("FITXA", ""+fitxaUsada);


        btncheck.setOnClickListener(new View.OnClickListener() {
            /**
             * Mètode que envia les dades a onResultActivity del color que s'ha escollit
             * @param v, representa una view
             *
             */
            @Override
            public void onClick(View v) {
                    int id = radiogroup1.getCheckedRadioButtonId();
                    if (id == -1) {
                        id = radiogroup2.getCheckedRadioButtonId();
                    }
                    // RadioButton radioButton = (RadioButton) findViewById(id);
                    Log.d("ID BUTTON", "" + id);
                    Intent resultData = new Intent();

                    switch (id) {
                        case R.id.radio0:
                            resultData.putExtra("color", "morado");

                            break;
                        case R.id.radio01:
                            resultData.putExtra("color", "azul");
                            break;
                        case R.id.radio02:
                            resultData.putExtra("color", "verde");
                            break;
                        case R.id.radio03:
                            resultData.putExtra("color", "rojo");
                            break;
                        case R.id.radio04:
                            resultData.putExtra("color", "amarillo");
                            break;
                        case R.id.radio05:
                            resultData.putExtra("color", "blanco");
                            break;

                    }
                    fitxaUsada = id;
                    setResult(RESULT_OK, resultData);
                    finish();
            }
        });


    }

    /**
     * Mètode que obre les opcions del dialeg en tornar enrera
     */
    @Override
    public void onBackPressed() {}



}
