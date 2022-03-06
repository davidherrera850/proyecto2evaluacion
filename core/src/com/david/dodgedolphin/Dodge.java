package com.david.dodgedolphin;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

// Clase que representa el juego
public class Dodge extends Game {
	private Preferences preferencias;
	public SpriteBatch batch;
	public ShapeRenderer debug;

	public int puntuacionMaxima = 0;
	public Skin skin;
	public static GestorDeRecursos gestorDeRecursos;

	public static final int WIDTH = 320;
	public static final int HEIGHT = 560;

	public BitmapFont fuente;
	public Audio audio;


	// creacion del juego
	@Override
	public void create () {
		batch = new SpriteBatch();
		debug = new ShapeRenderer();
		// creamos el componente audio
		audio = Gdx.audio;
		debug.setAutoShapeType(true);
		// obtenemos las fuentes de texto
		fuente = new BitmapFont(Gdx.files.internal("pixel.fnt"));
		skin = new Skin();
		// cargamos la skin
		gestorDeRecursos = new GestorDeRecursos(skin.current());
		// usamos las preferencias en score
		preferencias = Gdx.app.getPreferences("score");
		// obtenemos el valor maximo de puntuacion de las preferencias score
		puntuacionMaxima = preferencias.getInteger("maxScore");
		// establecemos la pantalla a mostrar, la actual
		this.setScreen(new PantallaInicial(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		Gdx.app.log("DodgeDolphing: ", "Disposing");
		batch.dispose();
		fuente.dispose();
		debug.dispose();
		gestorDeRecursos.dispose();
	}

	// funcion para guardar en las preferencias persistentes el valor maximo
	// de puntacion
	public void save(int score){
		puntuacionMaxima = score;
		preferencias.putInteger("maxScore", score);
		preferencias.flush();

	}
}
