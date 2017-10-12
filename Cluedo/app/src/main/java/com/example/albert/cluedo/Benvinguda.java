package com.example.albert.cluedo;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;

/**
 * Classe que representa un activitat de benvinguda
 */
public class Benvinguda extends AppCompatActivity {

    private Button btJuga;
    private Button btFons;
    private CheckBox cbMusica;
    private RelativeLayout layout;
    private Uri uri;
    static final private int PICK_IMAGE_REQUEST = 1;

    /**
     * Mètode onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benvinguda);
        final MediaPlayer mediaPlayer = new MediaPlayer();
        final Thread filSo = new Thread(new Runnable() {
            /**
             * Mètode que posa el marxa un thread que col·loca musica de fons
             */
            @Override
            public void run() {

                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                AssetFileDescriptor afd = getResources().openRawResourceFd(R.raw.musica_fons);
                try {
                    mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.setLooping(true);
                mediaPlayer.setVolume(1.0f, 1.0f);
            }
        });
        filSo.start();
        btJuga = (Button) findViewById(R.id.btJuga);
        cbMusica = (CheckBox) findViewById(R.id.cbMusica);
        cbMusica.setShadowLayer(1, 0, 0, Color.BLACK);
        btFons = (Button) findViewById(R.id.btFons);
        layout = (RelativeLayout) findViewById(R.id.layoutBenvinguda);
        //http://www.iteramos.com/pregunta/3335/getelegir-una-imagen-de-android-39-s-incorporado-galeria-aplicacion-mediante-programacion
        //https://github.com/Roberasd/Pictures-Android
        //https://www.youtube.com/watch?v=jFYAp42rMEA
        btFons.setOnClickListener(new View.OnClickListener() {
            /**
             * Mètode que en fer click obre la galeria
             * @param v, representa una view
             */
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                */


                //startActivityForResult(Intent.createChooser(intent, "Selecciona una foto"),PICK_IMAGE_REQUEST);


                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent.createChooser(intent, getResources().getString(R.string.SeleccionImatgen)), PICK_IMAGE_REQUEST);
            }
        });
        btJuga.setOnClickListener(new View.OnClickListener() {
            /**
             * Mètode que col·loca una imatge de fons
             * @param v, representa una view
             */
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Principal.class);
                if(uri!=null)
                    intent.putExtra("UriImatgeFons", uri.toString());
                startActivity(intent);
            }
        });

        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            /**
             * Mètode que prepara la música i la inicia
             * @param mp, representa un recurs d'audio
             */
            @Override
            public void onPrepared(MediaPlayer mp) {
                if(cbMusica.isChecked()){
                    mediaPlayer.start();
                }
            }
        });

        cbMusica.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /**
             * Mètode que encen o apaga la música depenent si el cehckbox esta marcat o no
             * @param buttonView, view del botó
             * @param isChecked, comprova si el checkbox esta marcat
             */
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cbMusica.isChecked()) {
                    //filSo.resume();
                    mediaPlayer.start();
                } else {
                    //filSo.stop();
                    mediaPlayer.pause();
                }
            }
        });

    }

    /**
     * Mètode que crea un menu
     * @param menu, representa un menú
     * @return true
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_benvinguda, menu);
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

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK &&
                data != null && data.getData() != null) {
            uri = data.getData();

            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                bitmapDrawable.setGravity(Gravity.CENTER);
                layout.setBackgroundDrawable(bitmapDrawable);

            }catch(IOException e){
                Toast toast = Toast.makeText(this, getResources().getString(R.string.ErrorAlCargarFoto), Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

}
