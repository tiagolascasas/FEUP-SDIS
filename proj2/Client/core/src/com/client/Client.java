package com.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.strongjoshua.console.Console;
import com.strongjoshua.console.GUIConsole;

public class Client extends ApplicationAdapter 
{
	private GUIConsole console;
	
	@Override
	public void create ()
	{
		this.console = new GUIConsole();
		console.setCommandExecutor(new Services(console));
		console.setVisible(true);
		console.setPosition(0, 700);
		console.setSize(700, 400);
	}

	@Override
	public void render () 
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.console.draw();
	}
	
	@Override
	public void dispose () 
	{
		console.dispose();
	}
}
