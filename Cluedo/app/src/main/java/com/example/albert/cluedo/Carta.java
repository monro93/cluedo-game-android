package com.example.albert.cluedo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.Normalizer;

/**
 * Classe abstracte que representa una carta
 */
public abstract class Carta {
    protected String nom;

    /**
     * Constructor amb un paràmetre
     * @param nom, representa el nom de la carta
     */
    public Carta(String nom){
        this.nom = nom;
    }

    /**
     * Mètode accessor
     * @return nom
     */
    public String getNom(){
        return nom;
    }

    /**
     * Mètode accessor que obté una view
     * @param context
     * @return new CartaView(context, tipus)
     */
    public CartaView getView(Context context){
        String tipus = this.getClass().getSimpleName();
        if(tipus.equals("Habitacio"))
            tipus = "Habitació";
        if(tipus.equals("Sospitos"))
            tipus = "Sospitós";
        return new CartaView(context, tipus);
    }

    /**
     * Classe interna que crea una view
     */
    private class CartaView extends LinearLayout {

        private TextView txtvTipus;
        private TextView txtvNom;
        private ImageView imatgeView;
        private String tipus;
        /**
         * Simple constructor to use when creating a view from code.
         *
         * @param context The Context the view is running in, through which it can
         *                access the current theme, resources, etc.
         */
        public CartaView(Context context, String tipus) {
            super(context);
            LayoutInflater inflater = LayoutInflater.from(context);
            inflater.inflate(R.layout.carta_layout, this);

            txtvTipus = (TextView) findViewById(R.id.txtViewCartaTipus);
            txtvNom = (TextView) findViewById(R.id.txtViewCartaNom);
            imatgeView = (ImageView) findViewById(R.id.imageViewCarta);
            tipus = tipus.toUpperCase();
            txtvTipus.setText(tipus);
            txtvNom.setText(getNom().toUpperCase());

            inicialitzarBackground();
            inicialitzarFoto();
        }

        /**
         * Mètode que afegeix el fons a la view de la carta
         */
        private void inicialitzarBackground(){
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.linearLayoutCarta);
            GradientDrawable shape = new GradientDrawable();
            shape.setStroke(1, Color.BLACK);
            shape.setCornerRadius(30);
            shape.setShape(GradientDrawable.RECTANGLE);
            shape.setColor(Color.parseColor("#0c0174"));
            shape.setSize(layout.getWidth(), layout.getHeight());
            layout.setBackground(shape);
        }

        /**
         * Mètode que afegeix una foto a la view de la carta
         */
        private void inicialitzarFoto(){
            String nom = Normalizer.normalize(getNom(), Normalizer.Form.NFD);
            nom = nom.toLowerCase();
            nom = nom.replace(" ","");
            nom = nom.replaceAll("[^\\p{ASCII}]", "");
            int id= getResources().getIdentifier(nom,"drawable", "com.example.albert.cluedo");
            Log.d("nom foto", nom);
            if(id != -1){
                Bitmap imatgeCarta = BitmapFactory.decodeResource(getResources(), id);
                imatgeView.setImageBitmap(imatgeCarta);
            }else{
                txtvNom.setTextSize(50);
            }
        }
    }
}
