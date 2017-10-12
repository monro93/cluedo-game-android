package com.example.albert.cluedo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Classe principal de l'aplicació
 */
public class Principal extends AppCompatActivity {
    private static int NUM_COLUMS = 24;
    private static int NUM_FILAS = 25;
    public static int ACTIVITY_BLOC_NOTES = 0;
    public static int ACTIVITY_BLOC_NOTES_ESCOLLINT = 1;
    public static int ACTIVITY_MENU_PRINCIPAL = 2;
    public static int ACTIVITY_MENU_PREGUNTA = 3;
    public static int ACTIVITY_MENU_ESCULL_CARTA = 4;
    public static int ACTIVITY_MENU_PREGUNTA_FINAL = 5;
    public static int ACTIVITY_BLOC_NOTES_FINAL = 6;
    public static int ACTIVITY_MENU_FINAL = 7;
    public static int ACTIVITY_ESCOLLIR_COLOR = 8;


    private Tauler grid;
    private Casella caselles[][];
    private ArrayList<Sospitos> sospitosos;
    private ArrayList<Arma> armas;
    private ArrayList<Habitacio> habitacions;
    private ArrayList<Fitxa> fitxes;
    private Jugador jugadorActual;
    private Jugador jugador1;
    private Jugador jugador2;
    private ArrayList<Carta> cartesABuscar;
    private ArrayList<Carta> cartesARepartir;
    private Dau dau;
    private Boolean DauTirat;
    private int numCasellesPotMoure;
    private int estatPartida;

    //protected Boolean nextSecretMovePlayer1=false;
    //protected Boolean nextSecretMovePlayer2=false;

    /**
     * Mètode onCreate que crida a les funcions
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        estatPartida = 0;

        vInicialitzarTauler();
        vInicialitzarEntitas();
        vInicialitzarFitxes();
        vEscollirFitxa();
        dau = new Dau();
        DauTirat = false;

    }

    /**
     * Mètode que envia un intent a DialegFitxes i permet escollir el color de la fitxa
     */
    protected void vEscollirFitxa() {
        Intent intent = new Intent(this, DialegFitxes.class);
        startActivityForResult(intent, ACTIVITY_ESCOLLIR_COLOR  );

    }


    /**
     * Mètode que inicialitza el tauler amb les mesures, els números de cada casella
     * i realitza accions en caure en els diferents tipus de casellas
     */
    protected void vInicialitzarTauler() {
        grid = new Tauler(new ContextThemeWrapper(this, R.style.styleGrid));
        caselles = new Casella[NUM_FILAS][NUM_COLUMS];

        //Trobar Mida caselles
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        TypedValue tv = new TypedValue();
        int actionBarHeight = (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) ?
                TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics()) : 0;
        int widthCasella = (size.x / NUM_COLUMS);
        int heightCasella = ((size.y - actionBarHeight) / NUM_FILAS) - 2;

        //Log.i("MEDIDAS CASELLA", "width: "+widthCasella+" height: "+heightCasella);
        FrameLayout scroll;
        if (widthCasella > heightCasella) {
            heightCasella = widthCasella;
            scroll = new ScrollView(this);
        } else {
            widthCasella = heightCasella;
            scroll = new HorizontalScrollView(this);
        }

        scroll.addView(grid);
        ((RelativeLayout) findViewById(R.id.relativeLayout1)).addView(scroll);
        //((RelativeLayout)findViewById(R.id.relativeLayout1)).addView(grid);

        grid.setColumnCount(NUM_COLUMS);
        grid.setRowCount(NUM_FILAS);

        int matriu[][] = {
                {0, 0, 0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0, 0, 2, 1, 2, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0},
                {3, 0, 0, 0, 0, 0, 4, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0},
                {2, 1, 1, 1, 1, 1, 1, 1, 1, 4, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 4, 0, 0, 0, 0, 0, 3},
                {2, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 4, 4, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 2},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 0, 4, 1, 1, 5, 5, 5, 5, 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
                {0, 0, 0, 0, 0, 0, 0, 1, 1, 5, 5, 5, 5, 5, 1, 1, 0, 4, 0, 0, 0, 0, 0, 0},
                {2, 0, 0, 4, 0, 0, 1, 1, 1, 5, 5, 5, 5, 5, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {2, 1, 1, 1, 1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 4, 0, 0, 0, 0, 1, 1, 1, 5, 5, 5, 5, 5, 1, 1, 4, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 1, 1, 5, 5, 5, 5, 5, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 1, 1, 5, 5, 5, 5, 5, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2},
                {2, 1, 1, 1, 1, 1, 1, 1, 0, 4, 0, 0, 0, 0, 4, 0, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 4, 0, 0, 0, 2},
                {2, 3, 0, 0, 4, 1, 1, 1, 4, 0, 0, 0, 0, 0, 0, 4, 1, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 2, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 2, 3, 0, 0, 0, 0, 0},
                {2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 0, 0, 0, 0, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2},
        };

        for (int x = 0; x < NUM_FILAS; x++) {
            for (int y = 0; y < NUM_COLUMS; y++) {
                Casella casella = new Casella(this);

                switch (matriu[x][y]) {
                    case 0:
                        casella = new Casella(new ContextThemeWrapper(this, R.style.casella), Casella.Tipus.MULTIPLE, x, y);
                        break;
                    case 1:
                        casella = new Casella(new ContextThemeWrapper(this, R.style.casella), Casella.Tipus.PASADIS, x, y);
                        break;
                    case 2:
                        casella = new Casella(new ContextThemeWrapper(this, R.style.casella), Casella.Tipus.NOJUGABLE, x, y);
                        break;
                    case 3:
                        casella = new Casella(new ContextThemeWrapper(this, R.style.casella), Casella.Tipus.DRAÇERA, x, y);
                        break;
                    case 4:
                        casella = new Casella(new ContextThemeWrapper(this, R.style.casella), Casella.Tipus.PORTA, x, y);
                        break;
                    case 5:
                        casella = new Casella(new ContextThemeWrapper(this, R.style.casella), Casella.Tipus.CENTRAL, x, y);
                        break;
                }
                caselles[x][y] = casella;
                casella.setMaxWidth(widthCasella);
                casella.setMinimumWidth(widthCasella);
                casella.setMaxHeight(heightCasella);
                casella.setMinimumHeight(heightCasella);

                grid.addView(casella);
            }


        }
        for (int i = 0; i < NUM_FILAS; i++) {
            for (int j = 0; j < NUM_COLUMS; j++) {
                caselles[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("kanag", v.toString());
                        Casella casellaClickada = (Casella) v;
                        Casella casellaAnterior = jugadorActual.getFitxa().getPosicio();
                        int xAnterior = casellaAnterior.getPosicioX();
                        int yAnterior = casellaAnterior.getPosicioY();
                        int x = casellaClickada.getPosicioX();
                        int y = casellaClickada.getPosicioY();


                        //while (numCasellesPotMoure > 0) {
                        Log.d("kanag", "" + (Math.abs(xAnterior - x) + Math.abs(yAnterior - y)));
                        if (casellaClickada != casellaAnterior && (casellaClickada.tipus == Casella.Tipus.PASADIS || casellaClickada.tipus == Casella.Tipus.PORTA || Casella.Tipus.CENTRAL ==casellaClickada.tipus)) {
                            if ((Math.abs(xAnterior - x) + Math.abs(yAnterior - y)) <= numCasellesPotMoure) {
                                Log.d("kanag", "es pot moure");
                                caselles[x][y].colocaFitxaACasella(jugadorActual.getFitxa());
                                if (casellaAnterior.tipus == Casella.Tipus.PASADIS && casellaClickada.tipus == Casella.Tipus.PORTA || (casellaAnterior.tipus == Casella.Tipus.PASADIS || casellaAnterior.tipus == Casella.Tipus.PORTA) && casellaClickada.tipus == Casella.Tipus.CENTRAL) {
                                    numCasellesPotMoure = 0;
                                } else if (casellaAnterior.tipus == Casella.Tipus.MULTIPLE &&
                                        casellaClickada.tipus == Casella.Tipus.MULTIPLE) {
                                } else{
                                    numCasellesPotMoure -= (Math.abs(xAnterior - x) + Math.abs(yAnterior - y));
                                }

                            } else {
                                Toast toast = Toast.makeText(getBaseContext(), "Només pots mouret " + numCasellesPotMoure + " caselles alhora.",
                                        Toast.LENGTH_SHORT);
                                toast.show();
                            }




                            if (Casella.Tipus.PORTA == casellaClickada.tipus) {
                                Boolean bAnteriorHabitacio = false;
                                for (Habitacio hab : habitacions) {
                                    if (hab.isCasellaInside(casellaAnterior))
                                        bAnteriorHabitacio = true;
                                }
                                if (!bAnteriorHabitacio) {
                                    Intent intent = new Intent(getApplicationContext(), DialegPregunta.class);
                                    for (Habitacio aux : habitacions) {
                                        if (aux.isCasellaInside(jugadorActual.getFitxa().getPosicio())) {
                                            intent.putExtra("habitacio", aux.getNom());

                                            startActivityForResult(intent, ACTIVITY_MENU_PREGUNTA);
                                        }
                                    }

                                }
                            } else if (Casella.Tipus.CENTRAL == casellaClickada.tipus && numCasellesPotMoure == 0) {

                                Intent intent = new Intent(getApplicationContext(), DialegPregunta.class);
                                intent.putExtra("RESOLDRE", true);
                                startActivityForResult(intent, ACTIVITY_MENU_PREGUNTA_FINAL);
                            } else if (numCasellesPotMoure <= 0) {
                                vJugarJoc();
                            }
                        } else {
                            Toast toast = Toast.makeText(getBaseContext(), "No pots arribar a aquesta posició.",
                                    Toast.LENGTH_SHORT);
                            toast.show();

                        }

                    }
                });
            }
        }

    }

    /**
     * Mètode que inicialitza els sospitosos, les armes i les habitacions
     */
    protected void vInicialitzarEntitas() {
        ArrayList<Casella> cList = new ArrayList<>();
        try {
            for (int x = 0; x < NUM_FILAS; x++) {
                for (int y = 0; y < NUM_COLUMS; y++)
                    cList.add(caselles[x][y]);
            }
            sospitosos = LectorXML.llegueixSospitosos(getAssets().open("sospitosos.xml"));
            armas = LectorXML.llegueixArmas(getAssets().open("armas.xml"));
            habitacions = LectorXML.llegueixHabitacions(getAssets().open("habitacions.xml"), cList);
            for(Habitacio hab: habitacions){
                Log.d("test", hab.getNom());
                hab.printarNomHabitacio();

            }
        } catch (IOException e) {
            Log.e("XML ERROR", "Error IO:" + e.getMessage() + "-" + e.getCause());
        }
    }

    /**
     * Mètode que reparteix les cartes als jugadors i selecciona al atzar les cartes
     * que s'hauran de buscar
     */
    protected void vReparteixCartes() {
        cartesARepartir = new ArrayList<>();
        cartesARepartir.addAll(sospitosos);
        cartesARepartir.addAll(armas);
        cartesARepartir.addAll(habitacions);

        cartesABuscar = new ArrayList<>();
        Carta auxi;

        auxi = sospitosos.get((int) (Math.random() * sospitosos.size()));
        cartesABuscar.add(auxi);
        cartesARepartir.remove(auxi);
        auxi = armas.get((int) (Math.random() * armas.size()));
        cartesABuscar.add(auxi);
        cartesARepartir.remove(auxi);
        auxi = habitacions.get((int) (Math.random() * habitacions.size()));
        cartesABuscar.add(auxi);
        cartesARepartir.remove(auxi);

        for (int i = 0; i < 9; i++) {
            auxi = cartesARepartir.get((int) (Math.random() * cartesARepartir.size()));
            Log.d("CartaJ1", "CJ1: " + auxi.getNom());
            cartesARepartir.remove(auxi);
            jugador1.afeguirCartaJugador(auxi);
        }
        for (int i = 0; i < 9; i++) {
            auxi = cartesARepartir.get((int) (Math.random() * cartesARepartir.size()));
            Log.d("CartaJ2", "CJ2: " + auxi.getNom());
            cartesARepartir.remove(auxi);
            jugador2.afeguirCartaJugador(auxi);
        }
        for (Carta cartes : cartesABuscar) {
            Log.d("CartaABuscar", "" + cartes.getNom());
        }
    }

    /**
     * Mètode que inicialitza les fitxes i les col·loca en la posicio inicial
     */
    protected void vInicialitzarFitxes() {
        fitxes = new ArrayList<>();
        ArrayList<Casella> casellesInici = new ArrayList<>();
        int posicions[][] = {{5, 0}, {0, 16}, {7, 23}, {18, 0}, {24, 9}, {24, 14}};
        String noms[] = {"morado", "rojo", "amarillo", "azul", "verde", "blanco"};
        int iterador = 0;
        for (Sospitos s : sospitosos) {
            fitxes.add(new Fitxa(noms[iterador],
                    s.getColor(),
                    caselles[posicions[iterador][0]][posicions[iterador][1]],
                    s
            ));
            iterador++;
        }
    }

    /**
     * Mètode que permet passar torn entre els objectes jugador
     */
    private void vPasarTorn() {
        if (jugadorActual.equals(jugador1)) {
            jugadorActual = jugador2;

        } else {
            jugadorActual = jugador1;
        }
        DauTirat = false;
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Te toca " + jugadorActual.getFitxa().getNom());
            /*dialog.setView(new LinearLayout(this) {{
                for (Carta c : jugadorActual.getCartesJugador()) {
                    addView(c.getView(getBaseContext()));
                }
            }});*/
        dialog.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                vJugarJoc();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        //vJugarJoc();
    }

    /**
     * Mètode que ens mostrar les cartes que l'altre jugador ens ha mostrat
     */

    private void vJugarJoc() {
        // dau.mostraDau(this);
        // numCasellesPotMoure = dau.getResultat();

        if((estatPartida == 0 && jugadorActual == jugador1)||
                (estatPartida == 1 && jugadorActual == jugador2)) {


            AlertDialog.Builder dialog = new AlertDialog.Builder(this);

            dialog.setTitle("Cartas que te han tocado:");
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    vJugarJoc();
                }
            });
            dialog.setView(new HorizontalScrollView(this) {{
                addView(new LinearLayout(this.getContext()) {{
                    for (Carta c : jugadorActual.getCartesJugador()) {
                        addView(c.getView(getBaseContext()));
                    }
                }});
            }});
            /*dialog.setView(new LinearLayout(this) {{
                for (Carta c : jugadorActual.getCartesJugador()) {
                    addView(c.getView(getBaseContext()));
                }

            }});*/
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    vJugarJoc();
                }
            });

            final AlertDialog alertDialog = dialog.create();
            alertDialog.setCanceledOnTouchOutside(false);
            //alertDialog.show();
            estatPartida++;
            if(jugadorActual==jugador1){
                AlertDialog.Builder dialog2 = new AlertDialog.Builder(this);

                dialog2.setTitle("TURNO DEL JUGADOR : " + jugadorActual.getFitxa().getNom().toUpperCase());
                dialog2.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.show();
                    }
                });
                AlertDialog alertDialog2 = dialog2.create();
                alertDialog2.setCanceledOnTouchOutside(false);
                alertDialog2.show();
            }else{
             alertDialog.show();
            }
        }
        else{
            if (jugadorActual.getProximaCartaAMostrar() != null) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                dialog.setTitle("T'han mostrat una carta!");
                dialog.setView(jugadorActual.getProximaCartaAMostrar().getView(this));
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        jugadorActual.setProximaCartaAMostrar(null);
                        vJugarJoc();
                    }
                });
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
                jugadorActual.setProximaCartaAMostrar(null);
            } else {
                Intent intent = new Intent(this, DialegPrincipal.class);
                //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("DauTirat", DauTirat);
                intent.putExtra("Name", jugadorActual.getFitxa().getNom().toUpperCase());
                for (Habitacio habita : habitacions) {
                    if (habita.getNom().equals("Garaje") || habita.getNom().equals("Terraza") || habita.getNom().equals("Baño") || habita.getNom().equals("Cocina")) {
                        if (habita.isCasellaInside(jugadorActual.getFitxa().getPosicio())) {
                            intent.putExtra("SaltEspecial", true);
                        }
                    }
                }


                startActivityForResult(intent, ACTIVITY_MENU_PRINCIPAL);
            }
        }

    }

    /**
     * Mètode que manega les peticions que es fan cap a altres activitats
     * @param requestCode, codi de la peticio
     * @param resultCode, resultat de la peticio
     * @param data, dades que es reben
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d("Result"," " + nextSecretMovePlayer2);
        //super.onActivityResult(requestCode, resultCode, data);
        // Log.d("UEEEEE","No hace el if");
        if ((requestCode == ACTIVITY_BLOC_NOTES) && (resultCode == RESULT_OK)) {
            ArrayList<String> myValue = data.getStringArrayListExtra("Marcats");
            jugadorActual.vActualitzaTachats(myValue);
            vJugarJoc();
            //Log.d("UEEEEEEEEEEE",myValue.toString());
            //     Log.d("UEEEEE","Hace el if");
        }

        if ((requestCode == ACTIVITY_MENU_PRINCIPAL) && (resultCode == RESULT_OK)) {
            String myValue = data.getStringExtra("Opcio");
            if (myValue.equals("Dau")) {
          /*      if(jugadorActual.equals(jugador1)){
                    nextSecretMovePlayer1=false;
                } else if(jugadorActual.equals(jugador2)){
                    nextSecretMovePlayer2=false;
                }*/
                dau.mostraDau(this);
                numCasellesPotMoure = dau.getResultat();
                DauTirat = true;
            } else if (myValue.equals("BlocNotes")) {
                jugadorActual.obrirBlocDeNotes();
            } else if (myValue.equals("PassarTorn")) {

                vPasarTorn();
            } else if (myValue.equals("PasadisSecret")) {
                for (Habitacio hab : habitacions) {
                    if (hab.isCasellaInside(jugadorActual.getFitxa().getPosicio())) {
                        if (hab.getNom().equals("Garaje")) {
                            //jugadorActual.getFitxa().moure(new Casella(this, Casella.Tipus.PORTA, 18, 25));
                            Log.d("Moviendo ficha: ", hab.getNom());
                            caselles[18][19].colocaFitxaACasella(jugadorActual.getFitxa());
                            break;
                        } else if (hab.getNom().equals("Terraza")) {
                            Log.d("Moviendo ficha: ", hab.getNom());
                            caselles[19][4].colocaFitxaACasella(jugadorActual.getFitxa());
                            break;
                            //jugadorActual.getFitxa().moure(new Casella(this, Casella.Tipus.PORTA, 4, 19));
                        } else if (hab.getNom().equals("Baño")) {
                            Log.d("Moviendo ficha: ", hab.getNom());
                            caselles[5][17].colocaFitxaACasella(jugadorActual.getFitxa());
                            break;
                        } else if (hab.getNom().equals("Cocina")) {
                            Log.d("Moviendo ficha: ", hab.getNom());
                            caselles[3][6].colocaFitxaACasella(jugadorActual.getFitxa());
                            break;

                        }

                    }
                }
                Intent intent = new Intent(getApplicationContext(), DialegPregunta.class);
                for (Habitacio aux : habitacions) {
                    if (aux.isCasellaInside(jugadorActual.getFitxa().getPosicio())) {
                        intent.putExtra("habitacio", aux.getNom());
                        startActivityForResult(intent, ACTIVITY_MENU_PREGUNTA);
                    }
                }


            }
        }

        final Jugador jugadorQueVull;
        if (jugadorActual == jugador1) {
            jugadorQueVull = jugador2;
        } else {
            jugadorQueVull = jugador1;
        }
        if ((requestCode == ACTIVITY_MENU_PREGUNTA) && (resultCode == RESULT_OK)) {
            Log.d("PREMUT", "He rebut algo.");
            final String HabitacioPremuda = data.getStringExtra("HabitacioPremuda");
            final String ArmaPremuda = data.getStringExtra("ArmaPremuda");
            final String SospitosPremut = data.getStringExtra("SospitosPremut");

//            Log.d("HABITACIO REBUDA",HabitacioPremuda);
//            Log.d("ARMA REBUDA", ArmaPremuda);
//            Log.d("SOSPITOS REBUDA", SospitosPremut);

            if (data.getBooleanExtra("Bloc", false)) {
                jugadorActual.obrirBlocDeNotes(false);
            } else {
                if (jugadorQueVull.BuscarCartaJugador(HabitacioPremuda) != null || jugadorQueVull.BuscarCartaJugador(ArmaPremuda) != null || jugadorQueVull.BuscarCartaJugador(SospitosPremut) != null) {
                    Toast toast = Toast.makeText(this, "El jugador té una carta que vols.", Toast.LENGTH_SHORT);
                    toast.show();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                    dialog.setTitle("Passa el Telefon.");
                    dialog.setMessage("Passa el Telefon a l'altre Jugador.");

                    dialog.setPositiveButton("FET!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getApplicationContext(), DialegEscullCarta.class);
                            //vJugarJoc();
                            if (jugadorQueVull.BuscarCartaJugador(ArmaPremuda) != null) {
                                intent.putExtra("Arma", ArmaPremuda);
                            } else {
                                intent.putExtra("Arma", "NULL");
                            }
                            if (jugadorQueVull.BuscarCartaJugador(SospitosPremut) != null) {
                                intent.putExtra("Sospitos", SospitosPremut);
                            } else {
                                intent.putExtra("Sospitos", "NULL");
                            }
                            if (jugadorQueVull.BuscarCartaJugador(HabitacioPremuda) != null) {
                                intent.putExtra("Habitacio", HabitacioPremuda);
                            } else {
                                intent.putExtra("Habitacio", "NULL");
                            }

                            startActivityForResult(intent, ACTIVITY_MENU_ESCULL_CARTA);
                        }
                    });


                    AlertDialog aDialog = dialog.create();

                    aDialog.setCanceledOnTouchOutside(false);
                    aDialog.show();


                } else {
                    vJugarJoc();
                }
                numCasellesPotMoure = 0;
            }

        }


        if ((requestCode == ACTIVITY_MENU_ESCULL_CARTA) && (resultCode == RESULT_OK)) {
            String cartaEscollida = data.getStringExtra("carta");
            jugadorActual.setProximaCartaAMostrar(jugadorQueVull.BuscarCarta(cartaEscollida));
            vPasarTorn();
        }

        if ((requestCode == ACTIVITY_BLOC_NOTES_ESCOLLINT) && (resultCode == RESULT_OK)) {
            ArrayList<String> myValue = data.getStringArrayListExtra("Marcats");
            jugadorActual.vActualitzaTachats(myValue);
            //vJugarJoc();
            if(jugadorActual.getFitxa().getPosicio().tipus == Casella.Tipus.CENTRAL) {
                Intent intent = new Intent(getApplicationContext(), DialegPregunta.class);
                intent.putExtra("RESOLDRE", true);
                startActivityForResult(intent, ACTIVITY_MENU_PREGUNTA_FINAL);
            }else{
                Intent intent = new Intent(getApplicationContext(), DialegPregunta.class);
                for (Habitacio aux : habitacions) {
                    if (aux.isCasellaInside(jugadorActual.getFitxa().getPosicio())) {
                        intent.putExtra("habitacio", aux.getNom());

                        startActivityForResult(intent, ACTIVITY_MENU_PREGUNTA);
                    }
                }
            }
        }

        if ((requestCode == ACTIVITY_MENU_PREGUNTA_FINAL) && (resultCode == RESULT_OK)) {
            Log.d("PREMUT", "He rebut algo.");
            final String HabitacioPremuda = data.getStringExtra("HabitacioPremuda");
            final String ArmaPremuda = data.getStringExtra("ArmaPremuda");
            final String SospitosPremut = data.getStringExtra("SospitosPremut");

            if (data.getBooleanExtra("Bloc", false)) {
                jugadorActual.obrirBlocDeNotes(false);
            }else{
                String arma = data.getStringExtra("ArmaPremuda");
                String sospitos = data.getStringExtra("SospitosPremut");
                String habitacio = data.getStringExtra("HabitacioPremuda");
                byte correcte = 0;
                for(Carta aux : cartesABuscar){
                    if(aux.getNom().equals(arma)){
                        correcte ++;
                    }else if(aux.getNom().equals(sospitos)){
                        correcte++;
                    }else if(aux.getNom().equals(habitacio)){
                        correcte++;
                    }
                }
                Intent intent = new Intent(getApplicationContext(), DialegFinal.class);
                if (correcte>=3){
                    intent.putExtra("Resultat", true);
                }else{
                    intent.putExtra("Resultat",false);
                }

                ArrayList<String> cartasFinales = new ArrayList<>();
                for (Carta cartes : cartesABuscar) {
                    cartasFinales.add(cartes.getNom());
                }
                intent.putExtra("CartasFinales",cartasFinales);


                startActivityForResult(intent,ACTIVITY_MENU_FINAL);
            }




//            Log.d("HABITACIO REBUDA",HabitacioPremuda);
//            Log.d("ARMA REBUDA", ArmaPremuda);
//            Log.d("SOSPITOS REBUDA", SospitosPremut);


        }
        if((requestCode==ACTIVITY_MENU_FINAL) && (resultCode == RESULT_OK)){
            Log.d("CartaABuscar final ", "finish");
            this.finish();
        }

        if ((requestCode == ACTIVITY_ESCOLLIR_COLOR) && (resultCode == RESULT_OK)) {

            String color = data.getStringExtra("color");
            if (jugador1 == null) {

                //Log.d("COLOR FITXA", color);

                for (Fitxa f : fitxes) {
                    if (f.getNom().equals(color)) {
                        Log.d("ENTRA COLOR FITXA", "");
                        jugador1 = new Jugador(f, this);
                    }
                }
                vEscollirFitxa();
            } else if (jugador2 == null) {

                for (Fitxa f : fitxes) {
                    if (f.getNom().equals(color)) {
                        Log.d("ENTRA COLOR FITXA", "");
                        jugador2 = new Jugador(f, this);
                    }
                }
                jugadorActual = jugador1;
                vReparteixCartes();
                //vPasarTorn();
                vJugarJoc();
            }

        }



    }
}
