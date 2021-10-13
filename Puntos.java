package com.example.proyecto;

import android.content.Context;

public class Puntos {
    private int puntosac =0;
    private MainActivity3 mainActivity3;

    public Puntos(Context context) {
        mainActivity3 = (MainActivity3) context;
    }
    public void setPuntosac(int puntosac) {
        this.puntosac = puntosac;
    }

    public int getPuntosac() {
        return this.puntosac;
    }
}
