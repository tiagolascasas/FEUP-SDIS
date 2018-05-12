package com.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.strongjoshua.console.GUIConsole;

public class Client extends ApplicationAdapter 
{
	private GUIConsole console;
	private Services services;
	
	@Override
	public void create()
	{
		console = new GUIConsole();
		console.setVisible(true);
		console.setPosition(0, 700);
		console.setSize(700, 400);
		
		FileHandle serverList = Gdx.files.local("servers.json");
		
		services = new Services(console, serverList);
		console.setCommandExecutor(services);
	}

	@Override
	public void render() 
	{
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.console.draw(); 
	}
	
	@Override
	public void dispose() 
	{
		console.dispose();
		if (ClientManager.getInstance().getConnected())
			services.close();
	}
}
