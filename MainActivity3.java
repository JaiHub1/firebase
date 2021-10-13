package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity3 extends AppCompatActivity {

    Button button;

    String playerName= "";
    String roomName= "";
    String role= "";
    String message= "";
    FirebaseDatabase database;
    DatabaseReference messageRef;

   private Button derecha;
   private Button izquierda;
   private Button girar;
   private Button jugar;
    private Juego juego;
    private boolean pausar = true;
    private MediaPlayer pantalla;
    private int pararpantalla;

   private TextView puntos;


   private Tablero tablajuego= new Tablero();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        button = findViewById(R.id.boton);
        button.setEnabled(false);

        database = FirebaseDatabase.getInstance();

        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        playerName = preferences.getString("playerName", "");

        Bundle extras = getIntent().getExtras();
        /*if (extras != null){
            roomName=extras.getString("roomName");
            if (roomName.equals(playerName))
            {
            role= "host";
        } else {
            role= "guest";

                }

            }

         */

        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                button.setEnabled(false);
                message = role + ":Jugando";
                messageRef.setValue(message);
            }
        });
        //escucha los mensajes entrantes
        messageRef =database.getReference("rooms/" + roomName + "/message");
        message = role + "Jugando";
        messageRef.setValue(message);
        addRoomEventListener();







        pantalla = new MediaPlayer();



        jugar = (Button) findViewById(R.id.boton);
        girar = (Button) findViewById(R.id.girar);
        derecha = (Button) findViewById(R.id.derecha);
        izquierda = (Button) findViewById(R.id.izquierda);
        puntos= (TextView) findViewById(R.id.puntos);

        juego = new Juego(this, tablajuego);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(480, 900);
        juego.setLayoutParams(params);
        LinearLayout relativeTetris = (LinearLayout) findViewById(R.id.linearLayout);
        juego.setBackgroundColor(Color.GRAY);
        relativeTetris.addView(juego);

        jugar.setOnClickListener(new View.OnClickListener()
          {
              int var =0;
              @Override
              public void onClick(View v) {
                  pararpantalla = pantalla.getCurrentPosition();
                  var++;

                  if(jugar.getText().equals("jugar")) {
                      pausar = false;

                      if(var ==1) {
                          pantalla.start();
                          pantalla.setLooping(true);
                      } else if(var >1) {
                          pantalla.seekTo(pararpantalla);
                          pantalla.start();
                      }
                  }


              }
          }



        );

    }

    private void addRoomEventListener()
    {
        messageRef.addValueEventListener (new ValueEventListener()
        {
           public void  onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (role.equals("host")) {
                   if(dataSnapshot.getValue(String.class).contains("guest:")) {
                       button.setEnabled(true);
                       Toast.makeText(MainActivity3.this, ""+ dataSnapshot.getValue(String.class).replace("guest:", ""), Toast.LENGTH_SHORT).show();
                   }
               } else {
                   if(dataSnapshot.getValue(String.class).contains("host:"))
                           {
                               button.setEnabled(true);
                               Toast.makeText(MainActivity3.this, ""+ dataSnapshot.getValue(String.class).replace("host:", ""), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
               messageRef.setValue("message");

            }

        });
    }

    public Button getDerecha() { return derecha;}
    public Button getIzquierda() { return izquierda;}
    public Button getGirar() { return girar; }
    public TextView getPuntos() { return puntos; }
    public MediaPlayer getPantalla() {  return pantalla; }

    public void onPause() {
        super.onPause();
        pausar = true;
        pantalla.stop();
    }
    public boolean getPausar() {  return pausar;}
    public void setPausar(boolean pause) { this.pausar=pause;}

}