package com.example.proyecto;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import android.media.MediaPlayer;
import android.widget.Toast;


public class Juego extends View implements View.OnClickListener{

    private MainActivity3 mainActivity3;
    private MediaPlayer pantalla;
    private Tablero tablajuego;
    private Puntos puntos;
    private Button derecha;
    private Button izquierda;
    private Button girar;
    private Button jugar;
    private  Timer tiempo = new Timer();
    private Random random = new Random();
    private ArrayList<figuras> listapieza;
    private final int puntuacion=10;
    private TextView puntuacionactual;

    private int timerPeriod =250;




    public Juego(Context context, Tablero tablajuego){
        super(context);
        this.mainActivity3 = (MainActivity3) context;
        this.tablajuego = tablajuego;
        listapieza = tablajuego.getPieceList();
        pantalla = mainActivity3.getPantalla();

        puntos = new Puntos(context);
        puntuacionactual = mainActivity3.getPuntos();
        puntuacionactual.append("0");

        girar = mainActivity3.getGirar();
        derecha = mainActivity3.getDerecha();

        izquierda = mainActivity3.getIzquierda();

        girar .setOnClickListener(this);
        derecha.setOnClickListener(this);
        izquierda.setOnClickListener(this);
        inicio();


    }


    public void inicio() {

        tiempo.schedule(new TimerTask() {

            @Override
            public void run() {
                mainActivity3.runOnUiThread(new TimerTask() {

                    @Override
                    public void run() {



                            tablajuego.movera(tablajuego.getCurrentPiece());

                           if (tablajuego.moverabaj(tablajuego.getCurrentPiece()) == false) {
                                int eliminfilas = tablajuego.limpiarf();
                                tablajuego.limpiarf();
                                listapieza.remove(tablajuego.getCurrentPiece());
                                listapieza.add(new figuras(random.nextInt(7) + 1));


                                if (eliminfilas > 0) {
                                    puntos.setPuntosac(puntos.getPuntosac() + eliminfilas * puntuacion);
                                    int p = puntos.getPuntosac();


                                    puntuacionactual.setText("Points:" +" "+ p);
                                    Toast toast1 =
                                            Toast.makeText(mainActivity3.getApplicationContext(),
                                                    "Ganaste!", Toast.LENGTH_SHORT);

                                    toast1.show();
                                    tiempo.cancel();
                                }

                            }
                            invalidate();

                    }
                });
            }
        },  0, timerPeriod);
    }


    public boolean lose() {

        if( tablajuego.fingame(tablajuego.getCurrentPiece())==true ) {
            tiempo.cancel();
            listapieza.clear();
            tablajuego.limpiartabla();
            mainActivity3.setPausar(true);
            pantalla.stop();
            Intent intent = new Intent(this.getContext(), MainActivity2.class);
            getContext().startActivity(intent);


            return true;
        }
        return false;
    }

    public void mostrarjuegol() {
        Intent intent = new Intent(this.getContext(), MainActivity2.class);
        getContext().startActivity(intent);
    }



    @Override
    public void onClick(View view) {
        //if(mainActivity.getPausar()==false) {

            switch(view.getId()) {
                case R.id.derecha:
                    tablajuego.moverD(tablajuego.getCurrentPiece());
                    invalidate();
                    break;
                case R.id.izquierda:
                    tablajuego.moverIz(tablajuego.getCurrentPiece());
                    invalidate();
                    break;

                case R.id.girar:
                tablajuego.rotar(tablajuego.getCurrentPiece());
                invalidate();
                break;

            }
        }
    //}
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        Paint tablero = new Paint();

        for (int x = 0; x < tablajuego.getBoardHeight(); x++) {
            for (int y = 0; y < tablajuego.getBoardWidth(); y++) {

                int color  = tablajuego.codeToColor(x,y);
                tablero.setColor(color);
                canvas.drawRect(y*30, x*30, y*30+30, x*30+30,tablero);
            }
        }
    }

    public Timer getTimer() {
        return this.tiempo;
    }


}
