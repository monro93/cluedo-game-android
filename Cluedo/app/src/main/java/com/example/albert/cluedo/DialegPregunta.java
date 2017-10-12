package com.example.albert.cluedo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class DialegPregunta extends Activity {

    private Spinner spSospitos;
    private Spinner spArma;
    private Spinner spHabitacio;
    private ArrayList<Sospitos> sospitosos;
    private ArrayList<Arma> armas;
    private ArrayList<String> habitacions;
    private ArrayList<Habitacio> habitaciones;
    private Button btOK;
    private Button btnBloc;
    private String sospitosSel;
    private String armaSel;
    private String habSel;
    private TextView tvTitol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialeg_pregunta);
        habitacions = new ArrayList<>();
        final Intent intent = getIntent();
        this.setFinishOnTouchOutside(false);
        spSospitos = (Spinner) findViewById(R.id.spSospitos);
        spArma = (Spinner) findViewById(R.id.spArma);
        spHabitacio = (Spinner) findViewById(R.id.spHabitacio);
        btOK = (Button) findViewById(R.id.btOkPregunta);
        btnBloc = (Button) findViewById(R.id.btnBloc);
        tvTitol = (TextView) findViewById(R.id.tvTitol);
        final Boolean RESOLDRE = intent.getBooleanExtra("RESOLDRE",false);


        if(RESOLDRE){
            btOK.setText("RESUELVE EL ENIGMA");
            tvTitol.setText("LA RESPUESTA ES...");
        }


        btnBloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultData = new Intent();
                resultData.putExtra("Bloc", true);
                setResult(RESULT_OK, resultData);
                finish();
            }
        });

        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultData = new Intent();
                resultData.putExtra("ArmaPremuda", armaSel);
                resultData.putExtra("SospitosPremut", sospitosSel);
                if(!RESOLDRE) {
                    resultData.putExtra("HabitacioPremuda", intent.getStringExtra("habitacio"));
                }else{
                    resultData.putExtra("HabitacioPremuda", habSel);
                }
                //Log.d("HABITACIO PREMUDA", intent.getStringExtra("habitacio"));
                //Log.d("ARMA PREMUDA", armaSel);
                //Log.d("SOSPITOS PREMUT", sospitosSel);
                setResult(RESULT_OK, resultData);
                //Log.d("He llegado", "He llegado :)");
                finish();


            }
        });





        try {
            sospitosos = LectorXML.llegueixSospitosos(getAssets().open("sospitosos.xml"));
            armas = LectorXML.llegueixArmas(getAssets().open("armas.xml"));
            habitaciones = LectorXML.llegueixHabitacions(getAssets().open("habitacions.xml"), new ArrayList<Casella>());

            ArrayAdapter<String> adapter;

            ArrayList<String> arrayListAuxiliar = new ArrayList<>();
            if(RESOLDRE){
                arrayListAuxiliar.add("-----");
                for(Habitacio s : habitaciones){
                    arrayListAuxiliar.add(s.getNom());
                    Log.d("aux ", arrayListAuxiliar.toString());
                }
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayListAuxiliar);
                spHabitacio.setAdapter(adapter);
            }else{
                arrayListAuxiliar.add("-----");
                habitacions.add(intent.getStringExtra("habitacio"));
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, habitacions);
                spHabitacio.setAdapter(adapter);
            }
            arrayListAuxiliar = new ArrayList<>();


            arrayListAuxiliar.add("-----");
            for(Sospitos s : sospitosos){
                arrayListAuxiliar.add(s.getNom());
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                    arrayListAuxiliar);
            spSospitos.setAdapter(adapter);

            arrayListAuxiliar = new ArrayList<>();

            arrayListAuxiliar.add("-----");
            for(Arma a : armas){
                arrayListAuxiliar.add(a.getNom());
            }
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                    arrayListAuxiliar);
            spArma.setAdapter(adapter);

        }catch(IOException e){
            Log.e("XML ERROR", "No es troba l'XML");
        }

        spSospitos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                habSel = spHabitacio.getSelectedItem().toString();
                armaSel = spArma.getSelectedItem().toString();
                sospitosSel = spSospitos.getSelectedItem().toString();
                if(!sospitosSel.equals("-----") && !armaSel.equals("-----") && !habSel.equals("-----")){
                    btOK.setEnabled(true);
                }else{
                    btOK.setEnabled(false);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                btOK.setEnabled(false);
            }
        });

        spArma.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                habSel = spHabitacio.getSelectedItem().toString();
                armaSel = spArma.getSelectedItem().toString();
                sospitosSel = spSospitos.getSelectedItem().toString();
                if(!sospitosSel.equals("-----") && !armaSel.equals("-----") && !habSel.equals("-----")){
                    btOK.setEnabled(true);
                }else{
                    btOK.setEnabled(false);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                btOK.setEnabled(false);
            }
        });

        if(RESOLDRE){
            spHabitacio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    habSel = spHabitacio.getSelectedItem().toString();
                    armaSel = spArma.getSelectedItem().toString();
                    sospitosSel = spSospitos.getSelectedItem().toString();

                    if(!sospitosSel.equals("-----") && !armaSel.equals("-----") && !habSel.equals("-----")){
                        btOK.setEnabled(true);
                    }else{
                        btOK.setEnabled(false);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    btOK.setEnabled(false);
                }
            });
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dialeg_pregunta, menu);
        return true;
    }

    @Override
    public void onBackPressed(){

    }
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
}
