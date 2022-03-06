package com.david.dodgedolphin;

import com.badlogic.gdx.utils.Array;

// clase para gestionar los diferentes skins posibles
public class Skin {
    // lista de skins disponibles
    private Array<String> skins = new Array<>();
    // el actual es el que esta en indice 0
    private int actual = 0;

    private long cooldown = 1000;

    private long tiempoActual = System.currentTimeMillis();

    // construtor por defecto
    public Skin(){

        // cargamos los dos skins disponibles
        skins.add("deep_ocean");
        skins.add("upper_ocean");
    }

    // para cambiar al siguiente skin (siguiente en la lista)
    public boolean next(){
        if (System.currentTimeMillis() - tiempoActual < cooldown)
            return false;
        tiempoActual = System.currentTimeMillis();
        actual = (actual < skins.size - 1) ? actual + 1 : 0;
        Dodge.gestorDeRecursos.setSkin(skins.get(actual));
        return true;
    }

    // para cambiar al skin previo (uno atras en la lista)
    public boolean prev(){
        if (System.currentTimeMillis() - tiempoActual < cooldown)
            return false;
        tiempoActual = System.currentTimeMillis();
        actual = (actual > 0) ? actual - 1 : skins.size - 1;
        Dodge.gestorDeRecursos.setSkin(skins.get(actual));
        return true;
    }

    // obtiene el skin actual
    public String current(){
        return skins.get(actual);
    }

}
