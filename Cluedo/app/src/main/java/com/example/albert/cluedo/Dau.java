package com.example.albert.cluedo;

import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import java.io.IOException;

/**
 * Classe que representa un dau
 */
public class Dau{

    int resultat;

    /**
     * Constructor per defecte
     */
    public Dau() {

    }

    /**
     * Mètode que mostra el dau i realitza una animació i un so al tirar el dau
     * @param context
     */
    public void mostraDau(final Context context){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dau_layout);
        Boolean bPrimerClick = true;

        final ImageView imatge = (ImageView) dialog.findViewById(R.id.ivDau);
        imatge.setBackgroundResource(R.drawable.animacions);

        final AnimationDrawable animacio = (AnimationDrawable)imatge.getBackground();
        animacio.setOneShot(true);
        final Handler handler = new Handler();
        final MediaPlayer mediaPlayer = new MediaPlayer();
        final Thread filSo = new Thread(new Runnable() {
            @Override
            public void run() {
                //MediaPlayer mediaPlayer = MediaPlayer.create(getContext(),R.raw.so_fitxa_mou);

                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                AssetFileDescriptor afd = context.getResources().openRawResourceFd(R.raw.so_dau);
                try {
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.setLooping(true);
                mediaPlayer.setVolume(1.0f, 1.0f);
                mediaPlayer.start();
            }
        });


        final Runnable run = new Runnable() {
            @Override
            public void run() {

                handler.removeCallbacks(this);
                if(mediaPlayer.isPlaying()&&mediaPlayer.isLooping()) {
                    mediaPlayer.setLooping(false);
                    mediaPlayer.pause();
                    mediaPlayer.release();

                }

                imatge.setOnClickListener(null);
                switch(resultat){
                    case 1:
                        imatge.setBackgroundResource(R.drawable.dado1);
                        break;
                    case 2:
                        imatge.setBackgroundResource(R.drawable.dado2);
                        break;
                    case 3:
                        imatge.setBackgroundResource(R.drawable.dado3);
                        break;
                    case 4:
                        imatge.setBackgroundResource(R.drawable.dado4);
                        break;
                    case 5:
                        imatge.setBackgroundResource(R.drawable.dado5);
                        break;
                    case 6:
                        imatge.setBackgroundResource(R.drawable.dado6);
                        break;

                }
                Log.d("Dau", "Dau resultat = " + resultat);
            }

        };

        
        imatge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animacio.start();
                if(filSo.getState() == Thread.State.NEW)
                    filSo.start();
                handler.postDelayed(run, 600);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.removeCallbacks(this);
                        dialog.dismiss();
                    }
                }, 2000);

                Log.d("dau", "test");

            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
        resultat = (int) (Math.random() * 6) + 1;
    }

    /**
     * Mètode accessor que obté el resultat de tirar el dau
     * @return resultat
     */
    public int getResultat(){
        return resultat;
    }


}
