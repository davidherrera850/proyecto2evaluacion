package com.david.dodgedolphin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;

import static com.david.dodgedolphin.Dodge.gestorDeRecursos;

// Clase que representa la pantalla inicial del juego
public class PantallaInicial implements Screen {


    private final Dodge juego;
    // camara del render
    private OrthographicCamera camara;
    private Texture fondoOceanico;
    private Texture cambiarSkin;
    private Texture flecha;
    private Sprite mensaje;
    private float factorDeEscalado = 1f;
    private Vector3 puntoPulsado;


    // Constructor
    public PantallaInicial(Dodge dodge) {
        this.juego = dodge;
        camara = new OrthographicCamera();
        // establecemos la camara en ortogonal (de frente)
        camara.setToOrtho(false, Dodge.WIDTH, Dodge.HEIGHT);
        puntoPulsado = new Vector3();
        // cargamos las texturas
        fondoOceanico = gestorDeRecursos.loadSkinTexture("background-day.png");
        mensaje = new Sprite(gestorDeRecursos.loadSkinTexture("message.png"));
        cambiarSkin = gestorDeRecursos.loadTexture("change_skin.png");
        flecha = gestorDeRecursos.loadTexture("arrow.png");
        // mensaje para cambiar el skin si se quiere
        mensaje.setPosition(Dodge.WIDTH / 2f - mensaje.getWidth() / 2f, Dodge.HEIGHT / 2f - mensaje.getHeight() / 5f);
    }


    @Override
    public void show() {
    }


    // Funcion de renderizado en cada frame
    @Override
    public void render(float delta) {
        // limpiamos los valores de colores de la pantalla.
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        // limpiamos el buffer de colores
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // establecemos la escala minima y maxima del mensaje
        if (mensaje.getScaleX() > 1.1f)
            factorDeEscalado = -5f * delta;
        if (mensaje.getScaleX() < 0.9f)
            factorDeEscalado = 5f * delta;

        // escalamos el componente de mensaje
        mensaje.scale(factorDeEscalado * delta);

        // actualizamos la camara
        camara.update();

        juego.batch.setProjectionMatrix(camara.combined);
        // comenzamos a montar la pantalla
        juego.batch.begin();
        // establecemos el fondo de pantalla primero
        juego.batch.draw(fondoOceanico, 0, 0, Dodge.WIDTH, Dodge.HEIGHT);
        mensaje.draw(juego.batch);
        // el componenten changeSkin
        juego.batch.draw(cambiarSkin, Dodge.WIDTH / 2f - cambiarSkin.getWidth() / 2f, 70);
        // flecha en la izquierda
        juego.batch.draw(flecha, Dodge.WIDTH - flecha.getWidth() - 20, 50);
        // flecha a la derecha
        juego.batch.draw(flecha, 20, 50, flecha.getWidth(), flecha.getHeight(), 0, 0, flecha.getWidth(), flecha.getHeight(), true, false);
        juego.batch.end();

        if (Gdx.input.isTouched())
            input(Gdx.input.getX(), Gdx.input.getY());

    }

    // para cada toque
    public void input(float x, float y) {

        camara.unproject(puntoPulsado.set(x, y, 0));
        // si hemos tocado dentro de play, creamos el juego con el delfin
        if (mensaje.getBoundingRectangle().contains(puntoPulsado.x, puntoPulsado.y)) {
            juego.setScreen(new PantallaDeJuego(juego));
            return;
        }
        // si hemos pulsado en la parte izquierda, cambiamos el skin
        if (puntoPulsado.x > Dodge.WIDTH / 2f){
            if (juego.skin.next()){
                juego.setScreen(new PantallaInicial(juego));
                return;
            }
        }
        // si hemos pulsado en la parte derecha, cambiamos el skin
        if (puntoPulsado.x < Dodge.WIDTH / 2f) {
            if (juego.skin.prev()) {
                juego.setScreen(new PantallaInicial(juego));
            }
        }
    }

    // funciones que tenemos que implementar por obligacion
    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
