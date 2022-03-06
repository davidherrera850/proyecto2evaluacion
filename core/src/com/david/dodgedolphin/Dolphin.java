package com.david.dodgedolphin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

// Representa al delfin
public class Dolphin {
    // posicion x e y del delfin
    public float posicionX = 0, posicionY = 0;

    // rotacion del delfin
    public float rotacionPersonaje = 0f;
    public float velocidadEnEjeY = 0, velocidadEnEjeX = 0f;
    public Rectangle cajaContenedoraDelfin;
    public float fuerzaGravedad = 9.807f * 2f;
    public int anchura, altura;
    public State estado;

    private static final float COOLDOWN = 0.5f;
    private float cooldown = COOLDOWN;
    private Array<TextureRegion> texturas = new Array<>();
    private int frame = 0;
    private float lastFrameTime = 0;
    private float graceTime = 0;

    public Sound muerto;
    public Sound nada;
    public Sound punto;


    public enum State {
        MUERTO,
        PERIODO_DE_GRACIA,
        JUGANDO
    }


    // Constructor por defecto del Delfin
    public Dolphin() {
        // cargamos las texturas
        texturas.add(new TextureRegion(Dodge.gestorDeRecursos.loadSkinTexture("dolphin.png")));
        texturas.add(new TextureRegion(Dodge.gestorDeRecursos.loadSkinTexture("dolphin.png")));
        texturas.add(new TextureRegion(Dodge.gestorDeRecursos.loadSkinTexture("dolphin.png")));

        // cargamos los efectos de sonido
        muerto = Dodge.gestorDeRecursos.loadSound("die.wav");
        nada = Dodge.gestorDeRecursos.loadSound("swim.ogg");
        punto = Dodge.gestorDeRecursos.loadSound("point.ogg");

        // establecemos la altura y anchura del delfin
        anchura = texturas.get(frame).getRegionWidth();
        altura = texturas.get(frame).getRegionHeight();

        // caja que contiene el delfin para simplificar las operaciones
        cajaContenedoraDelfin = new Rectangle(posicionX, posicionY, anchura, altura);
        // posicion de inicio del delfin
        posicionX = 20;
        // el delfin estara en la mitad de la pantalla
        posicionY = Dodge.HEIGHT / 2f;
        // comienza en periodo de gracia
        estado = State.PERIODO_DE_GRACIA;
    }


    // funciona para actualizar los valores de estado
    // en cada frame del juego. Recibe el tiempo transcurrido delta
    public void update(float delta) {
        // enfriamiento
        cooldown -= delta;
        lastFrameTime -= delta;
        // si no estamos muerto, incrementamos el frame.
        if (lastFrameTime < 0 && estado != State.MUERTO) {
            lastFrameTime = 1f / 12f;
            frame = (frame < 2) ? frame + 1 : 0;
        }

        // si estamos jugando aplicamos una velocidad del eje x
        // y una gravedad invertida, flotar
        if (estado == State.JUGANDO) {
            velocidadEnEjeX = 80f;
            fuerzaGravedad = -9.807f * 0.5f;
        }

        // si estamos en periodo de gracia
        // aplicamos una gravedad positiva
        if (estado == State.PERIODO_DE_GRACIA) {
            graceTime -= delta;
            fuerzaGravedad = 2f;
            if (cooldown < 0 && velocidadEnEjeY < 0) {
                cooldown = COOLDOWN;
                velocidadEnEjeY = 0;
                velocidadEnEjeY += 0.5f;
                velocidadEnEjeX = 80f;
            }
        }

        // si no estamos muerto y la posicion es abajo del todo
        // la arena, el delfin muere.
        if (estado != State.MUERTO && posicionY < 20) {
            estado = State.MUERTO;
            velocidadEnEjeX = 0f;
            muerto.play();
        }

        // aplicamos los cambios sobe la caja contenedora del delfin
        velocidadEnEjeY -= delta * fuerzaGravedad;
        cajaContenedoraDelfin.setPosition(posicionX, posicionY);


        // rotamos
        rotacionPersonaje = rotacionPersonaje * -delta * 3;
        for (int i = 0; i < 9; i++)
            if (velocidadEnEjeY < -i * 1.5f) rotacionPersonaje = (i * -10f);
        posicionY = MathUtils.clamp(posicionY + velocidadEnEjeY, 0, Dodge.HEIGHT);

    }

    // cuando se hace click en la pantalla
    public void input() {
        // si el estado actual es muerto
        if (estado == State.MUERTO) {
            return;
        }

        // si estamos en periodo de gracia, aplicamos una gravedad especial
        // y cambiamos el estado a jugando
        if (estado == State.PERIODO_DE_GRACIA && Gdx.input.justTouched() && graceTime < 0) {
            fuerzaGravedad = -9.807f * 0.5f;
            estado = State.JUGANDO;
            return;
        }

        // si estamos jugando aplicamos rotacion al pesonaje
        // y cambiamos su velocidad en Y
        if (estado == State.JUGANDO && Gdx.input.justTouched() && cooldown < 0) {
            velocidadEnEjeY = 0;
            velocidadEnEjeY -= 4f;
            cooldown = COOLDOWN;
            rotacionPersonaje = 25f;
            nada.play();
        }
    }

    // funcion para dibujar cada frame del juego
    public void draw(SpriteBatch batch) {
        batch.draw(texturas.get(frame), posicionX, posicionY, anchura / 2f, altura / 2f, anchura, altura, 1f, 1f, rotacionPersonaje);
    }


    // funcion que resetea los valores por defectos del juego
    //  como el estado, el periodo de gracia etc.
    public void reset() {
        posicionX = 20;
        posicionY = Dodge.HEIGHT / 2f;
        estado = State.PERIODO_DE_GRACIA;
        graceTime = 1f;
    }

}
