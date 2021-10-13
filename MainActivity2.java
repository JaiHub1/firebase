package com.example.proyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    ListView listView;
    Button button;

    List<String> roomsList;

    String playerName="";
    String roomName= "";

    FirebaseDatabase database;
    DatabaseReference roomRef;
    DatabaseReference roomsRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        database = FirebaseDatabase.getInstance();
        // obtiene el nombre del jugador y le pone el nombre del jugador a la sala creada
        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        playerName = preferences.getString("players", "Sala1");
        roomName =playerName;

        listView = findViewById(R.id.listview);
        button = findViewById(R.id.boton);


        roomsList =new ArrayList<>();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //crea la sala

                button.setText("Creando sala");
                button.setEnabled(false);
                roomName = playerName;
                roomRef = database.getReference("rooms/" + roomName+ " /player1");
                addRoomEventListener();
                roomRef.setValue(playerName);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //ingresar a la sala como jugador2
                roomName =  roomsList.get(position);
                roomName = roomName;
                roomRef = database.getReference("rooms/" + roomName+ " /player2");
                addRoomEventListener();
                roomRef.setValue(playerName);
            }
        });
        //mostrar si la nueva sala está
        addRoomsEventListener();
}
    private void addRoomEventListener()
    {
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //ingresar a la sala
               // btn.setText("Crear Sala");
                button.setEnabled(true);
                Intent intent = new Intent(getApplicationContext(), MainActivity3.class);
                intent.putExtra("roomName", roomName);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               // btn.setText("Crear Sala");
                button.setEnabled(true);
                Toast.makeText(MainActivity2.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

        private void addRoomsEventListener()
        {
        roomsRef = database.getReference("rooms");
        roomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //mostrar la lista
                roomsList.clear();
                Iterable<DataSnapshot> rooms = dataSnapshot.getChildren();
                for (DataSnapshot snapshot: rooms)
                {
                    roomsList.add(snapshot.getKey());

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity2.this, android.R.layout.simple_list_item_1, roomsList);
                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        }
}