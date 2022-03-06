package com.david.dodgedolphin;

import android.app.Activity;
import android.os.Bundle;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

// Esta clase se encarga de configurar la aplicación de android para adaptarla a un juego de libgdx
// https://libgdx.badlogicgames.com/ci/nightlies/docs/api/com/badlogic/gdx/backends/android/AndroidApplicationConfiguration.html
public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		/** This method has to be called in the {@link Activity#onCreate(Bundle)} method. It sets up all the things necessary to get
		 * input, render via OpenGL and so on. You can configure other aspects of the application with the rest of the fields in the
		 * {@link AndroidApplicationConfiguration} instance.
		 *
		 * @param listener the {@link ApplicationListener} implementing the program logic
		 * @param config the {@link AndroidApplicationConfiguration}, defining various settings of the application (use accelerometer,
		 *           etc.). */
		// iniciamos el juego Dodge con la configuracion de libgdx
		initialize(new Dodge(), config);
	}
}
