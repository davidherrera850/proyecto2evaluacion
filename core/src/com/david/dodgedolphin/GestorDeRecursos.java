package com.david.dodgedolphin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;
import java.util.Set;

public class GestorDeRecursos {


    private HashMap<String, Texture> mapDeTexturas = new HashMap<>();
    private HashMap<String, Sound> mapDeSonidos = new HashMap<>();
    private String skin;

    public GestorDeRecursos(String skin){
        this.skin = skin;
        // una textura por defecto siempre
        mapDeTexturas.put("fail", new Texture(Gdx.files.internal("_fail_.png")));
    }


    // funcion para cargar la skin en funcion del path que tengan
    public Texture loadSkinTexture(String path) {
        path = skin+ "/" + path;
        return loadTexture(path);
    }

    // funcion para cargar las texturas en un path
    public Texture loadTexture(String path) {
        if (mapDeTexturas.containsKey(path))
            return mapDeTexturas.get(path);

        FileHandle fileHandle = Gdx.files.internal(path);
        // si el fichero indicado no existe establecemos el de por defecto
        if (!fileHandle.exists()) return mapDeTexturas.get("fail");

        // cargamos la textura del path
        Texture texture = new Texture(fileHandle);
        mapDeTexturas.put(path, texture);
        // sacamos por debug que textura ha sido cargada
        Gdx.app.debug(getClass().getName(), "textura cargada: " + path);
        return texture;
    }

    // funcion para cargar los sonidos
    public Sound loadSound(String path){
        path = "soundEffects/" + path;
        if (mapDeSonidos.containsKey(path))
            return mapDeSonidos.get(path);

        // si el fichero indicado no existe establecemos el sonido por defecto
        FileHandle fileHandle = Gdx.files.internal(path);
        if (!fileHandle.exists()) return mapDeSonidos.get("fail");

        // cargamos los sonidos  del path
        Sound sound = Gdx.audio.newSound(fileHandle);
        mapDeSonidos.put(path, sound);
        // sacamos por debug que sonido ha sido cargada
        Gdx.app.debug(getClass().getName(), "sonido cargado: " + path);
        return sound;
    }

    public void dispose(){
        for (Texture value : mapDeTexturas.values())
            value.dispose();
        for (Sound value : mapDeSonidos.values())
            value.dispose();
        mapDeSonidos.clear();
        mapDeTexturas.clear();
    }

    // funcion para establecer la skin en funcion del path
    public void setSkin(String skin) {
        Gdx.app.debug(getClass().getName(), "Skin seleccionado: "+  skin);
        this.skin = skin;
        // set con las texturas
        Set<String> paths = mapDeTexturas.keySet();
        dispose();
        // paracada textura dentro de ese path, rellenamos el set
        for (String path : paths) {
            loadSkinTexture(path);
        }
    }

    private String skin(){
        return skin;
    }
}
