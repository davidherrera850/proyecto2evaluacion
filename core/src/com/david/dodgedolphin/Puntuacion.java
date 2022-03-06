package com.david.dodgedolphin;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;


public class Puntuacion {
    private int puntuacion = 0;
    public int anchura, altura;

    private int PADDING = 5;
    private IntArray listaDeDigitos;

    private Array<Texture> textures = new Array<>();
    private Texture buffer;

    // constructor por defecto
    public Puntuacion(){
        // cargamos las texturas
        for (int i = 0; i < 10; i++)
            textures.add(Dodge.gestorDeRecursos.loadTexture(i + ".png"));
        listaDeDigitos = new IntArray();
        // por defecto la puntuacion es 0
        puntuacion = 0;
    }

    // la funcion de dibujo para la puntuacion
    public void draw(SpriteBatch batch, float x, float y){
        // tomamos las texturas de la fuente
        for (int i = 0; i < listaDeDigitos.size; i++) {
            buffer = textures.get(listaDeDigitos.get(i));
            batch.draw(buffer, x + (buffer.getWidth() + PADDING) * i, y);
        }

    }

    // establecemos la puntuacion
    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
        listaDeDigitos.clear();

        anchura = 0;
        if (puntuacion == 0){
            listaDeDigitos.add(0);
            anchura += textures.get(0).getWidth() + PADDING;
            altura = textures.get(0).getHeight();
        }

        for (int i = puntuacion; i > 0; i /= 10){
            listaDeDigitos.add(i % 10);
            anchura += textures.get(i % 10).getWidth() + PADDING;
            altura = textures.get(i % 10).getHeight();
        }

        // le damos la vuelta a la lista de digitos,
        // porque la puntuacion la hemos metido de derecha a izquierda.
        listaDeDigitos.reverse();
    }

    public int getPuntuacion() {
        return puntuacion;
    }
}
