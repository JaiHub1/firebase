package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {


    EditText editText;
    Button button;

    String playerName= "";

    FirebaseDatabase database;
    DatabaseReference playerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        button =findViewById(R.id.boton);

        database = FirebaseDatabase.getInstance();

        //mirar si el jugardor existe y obtenerlo
       /* SharedPreferences preferences = getSharedPreferences("PREFS", 0 );
        playerName = preferences.getString("PlayerName", "");
        if(!playerName.equals("")){
            playerRef = database.getReference("players/"+ playerName);
            addEventListener();
            playerRef.setValue("");
        }

        */

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playerName = editText.getText().toString();
                editText.setText("");
                if(!playerName.equals(""))
                {
                    button.setText("Entrando");
                    button.setEnabled(false);
                    playerRef = database.getReference("Jugadores/"+ playerName);
                    addEventListener();
                    playerRef.setValue("");
            }

                }
        });


        }
    private void addEventListener() {
        //leer de la base
        playerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!playerName.equals(""))
                {
                    SharedPreferences preferences = getSharedPreferences("PREFS", 0);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("PlayerName", playerName);
                    editor.apply();

                    startActivity (new Intent(getApplicationContext(),MainActivity2.class));
                    finish();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                button.setText("ingresado");
                button.setEnabled(true);
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();


            }
        });
    }

        }


