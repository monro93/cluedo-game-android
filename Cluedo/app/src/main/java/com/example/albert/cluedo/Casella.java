package com.example.albert.cluedo;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.ImageView;

import java.io.IOException;

/**
 * Classe que representa una Casella
 */
public class Casella extends ImageView {
    public static enum Tipus{
        MULTIPLE,//0
        PASADIS,//1
        NOJUGABLE,//2
        DRAÇERA,//3
        PORTA,//4
        CENTRAL //5

    }
    Tipus tipus;
    int posicioX;
    int posicioY;

    /**
     * Constructor amb quatre paràmetres
     * @param context, representa els recursos
     * @param tipus, representa el tipus de casella
     * @param x, representa la  posició de x
     * @param y, representa la  posició de xy
     */
    public Casella(Context context, Tipus tipus, int x, int y ) {
        super(context);
        this.tipus = tipus;
        this.posicioX = x;
        this.posicioY = y;
        BackgroundPerDefecte();
        //introduirText("A");
    }

    /**
     * Mètode que dibuixa cada part del background
     */
    public void BackgroundPerDefecte(){
        this.setImageResource(0);
        GradientDrawable shape = new GradientDrawable();
        switch(tipus){
            case MULTIPLE:
                shape.setColor(Color.WHITE);
                break;
            case PASADIS:
                shape.setStroke(1, Color.BLACK);
                shape.setCornerRadius(0);
                shape.setShape(GradientDrawable.RECTANGLE);
                shape.setColor(Color.GRAY);
                break;
            case NOJUGABLE:
                shape.setColor(Color.BLACK);
                break;
            case DRAÇERA:
                shape.setStroke(1, Color.BLACK);
                shape.setCornerRadius(0);
                shape.setShape(GradientDrawable.RECTANGLE);
                shape.setColor(Color.YELLOW);

                break;
            case CENTRAL:
                shape.setColor(Color.parseColor("#EB0D1B5F"));
                break;
            case PORTA:

                shape.setColor(Color.WHITE);
                this.setBackground(shape);
                //int id= getResources().getIdentifier("puerta1","drawable", "com.example.albert.cluedo");
                Bitmap imatgePortaOriginal = BitmapFactory.decodeResource(getResources(), R.drawable.puerta3);

                //imatgePortaOriginal.setHeight(getHeight());
                //imatgePortaOriginal.setWidth(getWidth());
                Bitmap imatgePorta = Bitmap.createBitmap(imatgePortaOriginal.getWidth(), imatgePortaOriginal.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvasPorta = new Canvas(imatgePorta);
                if((getPosicioX()==8&&getPosicioY()==6)||(getPosicioX()==15&&getPosicioY()==5)||
                        (getPosicioX()==19&&getPosicioY()==15)||(getPosicioX()==19&&getPosicioY()==4)){
                    canvasPorta.rotate(-90, imatgePortaOriginal.getWidth()/2, imatgePortaOriginal.getHeight()/2);

                }else if((getPosicioX()==12&&getPosicioY()==16)||(getPosicioX()==20&&getPosicioY()==8)||
                        (getPosicioX()==4&&getPosicioY()==9)||(getPosicioX()==19&&getPosicioY()==8)){
                    canvasPorta.rotate(90, imatgePortaOriginal.getWidth()/2, imatgePortaOriginal.getHeight()/2);

                }else if((getPosicioX()==9&&getPosicioY()==17)||(getPosicioX()==12&&getPosicioY()==1)||
                        (getPosicioX()==17&&getPosicioY()==9)||(getPosicioX()==17&&getPosicioY()==14)||
                        (getPosicioX()==18&&getPosicioY()==19)){
                    canvasPorta.rotate(180, imatgePortaOriginal.getWidth()/2, imatgePortaOriginal.getHeight()/2);
                }

                canvasPorta.drawBitmap(imatgePortaOriginal, 0, 0, null);

                setScaleType(ScaleType.FIT_XY);
                setAdjustViewBounds(true);

                setImageBitmap(imatgePorta);

                //BitmapDrawable ob = new BitmapDrawable(getResources(), imatgePorta);
               // this.setBackground(ob);
           //     setBackgroundResource(new BitmapDrawable(getResources(), imatgePorta));
                //setImageResource(id);
                break;
        }
        if(tipus!= Tipus.PORTA)
            this.setBackground(shape);


    }

    /**
     * Constructor amb un paràmetre
     * @param context, representa el recurs.
     */
    public Casella(Context context){
        super(context);
    }

    /**
     * Mètode que comprova les posicions de x i y
     * @param x, representa la posició de x
     * @param y, representa la posició de y
     * @return
     */
    public boolean bIsALaPosicio(int x, int y){
        if(posicioX == x && posicioY == y)
            return true;
        return false;
    }

    /**
     * Mètode accessor que obté la posició de x
     * @return posicioX
     */
    public int getPosicioX(){
        return posicioX;
    }

    /**
     * Mètode accessor que obté la posició de y
     * @return posicioY
     */
    public int getPosicioY(){
        return posicioY;
    }


    /**
     * Mètode que introdueix text a la casella
     * @param text, representa el text que s'introduiex
     */
    public void introduirText(String text){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(30);
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(50, 50, conf);
        Canvas canvas = new Canvas(bmp);
        canvas.drawText(text, 0, 30, paint);

        this.setImageBitmap(bmp);

    }

    /**
     * Mètode que reprodueix un so al moure una fitxa
     * @param f, representa una fitxa
     */
    public void colocaFitxaACasella(Fitxa f){
        Thread filSo = new Thread(new Runnable() {
            @Override
            public void run() {
                //MediaPlayer mediaPlayer = MediaPlayer.create(getContext(),R.raw.so_fitxa_mou);
                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                AssetFileDescriptor afd = getContext().getApplicationContext().getResources().openRawResourceFd(R.raw.so_fitxa_mou);
                try {
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.setLooping(false);
                mediaPlayer.setVolume(1f, 1f);
                mediaPlayer.start();
            }
        });
        filSo.start();
        Casella anterior = f.posicio;
        anterior.BackgroundPerDefecte();

        f.posicio = this;
        int id= getResources().getIdentifier("fichatablero"+f.getNom(),
                "drawable", "com.example.albert.cluedo");


        setScaleType(ScaleType.FIT_XY);
        setAdjustViewBounds(true);
        setImageResource(id);
    }
}
