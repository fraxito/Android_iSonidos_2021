package com.example.isonidos;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.VideoView;

import java.lang.reflect.Field;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    HashMap<String , String> listaSonidos = new HashMap<String,String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Field[] listaCanciones = R.raw.class.getFields();
        LinearLayout principal = (LinearLayout) findViewById(R.id.botones);
        LinearLayout auxiliar =null;
        int num_columnas = 5; //numero de columnas en la vista
        //creamos la lista de botones que se irán añadiendo a la vista
        //en el layout donde van los botones
        for (int i=0; i<listaCanciones.length; i++){
            if (i % num_columnas == 0){
                auxiliar = creaLineaBotones(i);
                principal.addView(auxiliar);
            }
            //creamos un botón por código
            Button b = creaBoton(i, listaCanciones);
            listaSonidos.put(b.getTag().toString(), b.getText().toString());
            auxiliar.addView(b);
        }

    }

    private LinearLayout creaLineaBotones(int numeroLinea){
        LinearLayout.LayoutParams parametros =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                                              ,LinearLayout.LayoutParams.WRAP_CONTENT);
        parametros.weight=1;
        LinearLayout linea = new LinearLayout(this);
        linea.setLayoutParams(parametros);
        linea.setOrientation(LinearLayout.HORIZONTAL);
        linea.setId(numeroLinea);
        return linea;
    }

    private Button creaBoton(int i, Field[] _listaCanciones){
        LinearLayout.LayoutParams parametroBotones =
                new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
        parametroBotones.weight = 1;
        parametroBotones.setMargins(5,5,5,5);
        parametroBotones.gravity = Gravity.CENTER_HORIZONTAL;
        Button b = new Button(this);
        b.setLayoutParams(parametroBotones);
        b.setText (acortaEtiquetaBoton(_listaCanciones[i].getName()));
        b.setTextColor(Color.WHITE);
        b.setAllCaps(true);
        b.setBackgroundColor(Color.BLUE);
        int id= this.getResources().getIdentifier(_listaCanciones[i].getName(),"raw",this.getPackageName());
        b.setTag(id);
        b.setId(id + 500);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reproduceVideo(view);
            }
        });

        return b;
    }


    public void reproduceVideo(View vista){
       int idSonido = this.getResources().getIdentifier(vista.getTag().toString(),"raw", this.getPackageName());
        VideoView video = (VideoView) findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+idSonido);
        video.setVideoURI(uri);
        video.start();
    }

    public void reproduceSonido(View vista){
        MediaPlayer reproductor = new MediaPlayer();
        int idSonido = this.getResources().getIdentifier(vista.getTag().toString(),"raw", this.getPackageName());
        reproductor = MediaPlayer.create(this, idSonido);
        reproductor.start();
        reproductor.setOnCompletionListener(mediaPlayer -> {
            mediaPlayer.stop();
            if (mediaPlayer != null){
                mediaPlayer.release();
            }
        });
    }

    private String acortaEtiquetaBoton(String s){
        if (s.substring(0,2).contains("v_")){ //quita el primer v_
            s = s.substring(2);
        }
        if (s.contains("_")){
            s = s.substring(s.indexOf('_')); // quita lo siguiente al v_
        }
        s = s.replace('_', ' ');  //cambia los guiones bajos por espacios
        return s;
    }
}