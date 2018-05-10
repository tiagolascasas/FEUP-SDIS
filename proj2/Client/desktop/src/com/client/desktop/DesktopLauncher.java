package com.client.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.client.Client;

public class DesktopLauncher 
{
	public static void main (String[] arg) 
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 400;
		config.width = 700;
		config.resizable = false;
		new LwjglApplication(new Client(), config);
	}
}
