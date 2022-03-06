package com.david.dodgedolphin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import static com.david.dodgedolphin.Dodge.gestorDeRecursos;


// Clase que representa la pantalla de juego
public class PantallaDeJuego implements Screen {

    private final Dodge juego;
    private OrthographicCamera camara;
    private Dolphin delfin;
    private Puntuacion puntuacion;
    private Texture texturaFondoOceanico;
    private Texture texturaBase;
    private Texture texturaFinJuego;
    private Texture texturaMenu;
    private float PIEDRA_DISTANCIA_X = 200;
    private float PIEDRA_DISTANCIA_y = 200;

    private float[] basesX = new float[3];
    private Array<Rectangle> piedas = new Array<>();
    private Texture texturaPiedas;
    private Rectangle buffer;
    private Rectangle menu;
    private Vector3 puntoTocado;


    private GlyphLayout layout;

    public PantallaDeJuego(Dodge juego) {
        this.juego = juego;

        // inicializamos la camara a ortografica
        camara = new OrthographicCamera();
        camara.setToOrtho(false, Dodge.WIDTH, Dodge.HEIGHT);

        // creamos el delfin y la puntuacion
        delfin = new Dolphin();
        puntuacion = new Puntuacion();
        //cargamos las texturas y recursos
        texturaFondoOceanico = gestorDeRecursos.loadSkinTexture("background.png");
        texturaBase = gestorDeRecursos.loadSkinTexture("base.png");
        texturaPiedas = gestorDeRecursos.loadSkinTexture("tube.png");
        texturaFinJuego = gestorDeRecursos.loadSkinTexture("gameover.png");
        texturaMenu = gestorDeRecursos.loadTexture("menu.png");
        // creamos el menu como rectangulo a la izquierda arriba
        menu = new Rectangle(10, Dodge.HEIGHT - 10 - texturaMenu.getHeight() , texturaMenu.getWidth(), texturaMenu.getHeight());
        puntoTocado = new Vector3();
        layout = new GlyphLayout();

        // Creamos los obstaculos de piedra
        for (int i = 0; i < basesX.length; i++)
            basesX[i] = i * texturaBase.getWidth();

        Rectangle rectangle = new Rectangle(10, Dodge.HEIGHT + 10 , texturaPiedas.getWidth(), texturaPiedas.getHeight());

        // añadimos los obstáculos
        buffer = new Rectangle();
        piedas.add(new Rectangle(rectangle));
        piedas.add(new Rectangle(rectangle));
        piedas.add(new Rectangle(rectangle));
        piedas.add(new Rectangle(rectangle));

        // Establecemos cada pieda de diferente altura
        for (int i = 0; i < piedas.size; i++) {
            piedas.get(i).x = 500 + PIEDRA_DISTANCIA_X * i;
            piedas.get(i).y = -MathUtils.random(90, 260);
        }


    }

    @Override
    public void show() {
    }

    // funcion de renderizado

    @Override
    public void render(float delta) {
        // si el delfin esta muerto
        // sacamos la puntuacion y guardamos los puntos
        if (delfin.estado == Dolphin.State.MUERTO) {
            if (puntuacion.getPuntuacion() > juego.puntuacionMaxima){
                juego.save(puntuacion.getPuntuacion());
            }
            // y si ha tocado, reiniciamos
            if (Gdx.input.justTouched()){
                reset();
            }
        }

        // volvemos a la pantalla inicial
        if (Gdx.input.isTouched()) {
            camara.unproject(puntoTocado.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (menu.contains(puntoTocado.x, puntoTocado.y)){
                juego.setScreen(new PantallaInicial(juego));
            }
        }

        // actualizamos la pantalla
        delfin.input();
        delfin.update(delta);


        float mostRight = 0f;

        // buscamos la piedra más a la derecha, la que tiene mayor x
        for (Rectangle tube : piedas)
            mostRight = Math.max(tube.x, mostRight);

        // para cada obstaculo establecemos
        for (Rectangle piedra : piedas) {
            if (piedra.x <= -texturaPiedas.getWidth()) {
                piedra.x = mostRight + PIEDRA_DISTANCIA_X;
                puntuacion.setPuntuacion(puntuacion.getPuntuacion() + 1);
                delfin.punto.play();
            }
            // si estamos jugando restamos la posicion x para mostrarla en la pantalla actual.
            if (delfin.estado == Dolphin.State.JUGANDO)
                piedra.x -= delfin.velocidadEnEjeX * delta;
            buffer.set(piedra.x, piedra.y + piedra.height + PIEDRA_DISTANCIA_y, piedra.width, piedra.height);

            // si la piedra colisiona con el delfin por cualquiera de sus dimensiones
            // el delfin muere
            if ((piedra.overlaps(delfin.cajaContenedoraDelfin) || buffer.overlaps(delfin.cajaContenedoraDelfin)) && delfin.estado != Dolphin.State.MUERTO) {
                delfin.estado = Dolphin.State.MUERTO;
                delfin.muerto.play();
            }
        }

        mostRight = 0f;
        // movimiento de la arena oceanica
        for (float x : basesX)
            mostRight = Math.max(x, mostRight);


        for (int i = 0; i < basesX.length; i++) {
            if (basesX[i] <= -texturaBase.getWidth())
                basesX[i] = mostRight + texturaBase.getWidth();
            if (delfin.estado != Dolphin.State.MUERTO)
                basesX[i] -= delfin.velocidadEnEjeX * delta;
        }


        // restablecemos los colores
        Gdx.gl.glClearColor(0, 0, 0, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camara.update();
        juego.batch.setProjectionMatrix(camara.combined);

        juego.batch.begin();
        juego.batch.draw(texturaFondoOceanico, 0, 0, Dodge.WIDTH, Dodge.HEIGHT);

        // establecemos en el batch las piedras
        for (int i = 0; i < piedas.size; i++) {
            juego.batch.draw(texturaPiedas, piedas.get(i).x, piedas.get(i).y);
            juego.batch.draw(texturaPiedas, piedas.get(i).x, piedas.get(i).y + texturaPiedas.getHeight() + PIEDRA_DISTANCIA_y,  //this monstrosity just to flip texture on y
                    texturaPiedas.getWidth(), texturaPiedas.getHeight(), 0, 0,
                    texturaPiedas.getWidth(), texturaPiedas.getHeight(), false, true
            );
        }

        for (float x : basesX)
            juego.batch.draw(texturaBase, x, -70f);

        // si el delfin esta muerto mostramos la puntuacion maxima de todas las partidas
        if (delfin.estado == Dolphin.State.MUERTO) {
            layout.setText(juego.fuente, "Max_Score_in_all_games: " + juego.puntuacionMaxima);
            juego.batch.draw(texturaFinJuego, Dodge.WIDTH / 2f - texturaFinJuego.getWidth() / 2f, Dodge.HEIGHT / 2f - texturaFinJuego.getHeight() / 2f);
            juego.fuente.draw(juego.batch, layout, Dodge.WIDTH / 2f - layout.width / 2, Dodge.HEIGHT / 2f - layout.height / 2f - texturaFinJuego.getHeight());
        }

        puntuacion.draw(juego.batch, Dodge.WIDTH / 2f - puntuacion.anchura / 2f, Dodge.HEIGHT - 100);
        juego.batch.draw(texturaMenu, menu.x, menu.y);

        delfin.draw(juego.batch);
        juego.batch.end();
    }

    // funcion para restablecer los valores de la partida
    private void reset() {
        puntuacion.setPuntuacion(0);
        piedas.clear();
        delfin.reset();
        Rectangle rectangle = new Rectangle(0, 0, texturaPiedas.getWidth(), texturaPiedas.getHeight());

        // generamos los obstaculos
        piedas.add(new Rectangle(rectangle));
        piedas.add(new Rectangle(rectangle));
        piedas.add(new Rectangle(rectangle));
        piedas.add(new Rectangle(rectangle));

        // genermaos piedras al azar entre rangos delimitados por nosotros
        for (int i = 0; i < piedas.size; i++) {
            piedas.get(i).x = 500 + PIEDRA_DISTANCIA_X * i;
            piedas.get(i).y = -MathUtils.random(100, 250);
        }

    }

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
