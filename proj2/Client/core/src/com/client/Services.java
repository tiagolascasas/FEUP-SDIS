package com.client;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.client.requests.RequestConnect;
import com.client.requests.RequestDownload;
import com.client.requests.RequestLogin;
import com.client.requests.RequestSearch;
import com.client.requests.RequestUpload;
import com.strongjoshua.console.CommandExecutor;
import com.strongjoshua.console.GUIConsole;
import com.strongjoshua.console.annotation.ConsoleDoc;

public class Services extends CommandExecutor 
{
	private ServerListener listener;
	
	public Services(GUIConsole console)
	{
		ClientManager.getInstance().setConsole(console);
	}
	
	@ConsoleDoc(description = "Establishes a connection to the server")
	public void connect(String ip, int port)
	{
		RequestConnect request = new RequestConnect(ip, port);
		request.run();
	}
	
	@ConsoleDoc(description = "Logs the user onto the server")
	public void login(String username, String password)
	{
		RequestLogin request = new RequestLogin(username, password);
		request.run();
	}
	
	@ConsoleDoc(description = "Uploads a track to the server")
	public void upload(String track)
	{
		RequestUpload request = new RequestUpload(track);
		request.run();
	}
	
	@ConsoleDoc(description = "Downloads and plays the specified track")
	public void play(String track)
	{
		RequestDownload request = new RequestDownload(track);
		request.run();
	}
	
	@ConsoleDoc(description = "Searches for the specified track")
	public void search(String title)
	{
		RequestSearch request = new RequestSearch(title);
		request.run();
	}
	
	@ConsoleDoc(description = "Closes the application")
	public void exit()
	{
		try
		{
			ClientManager.getInstance().getSocket().close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
		Gdx.app.exit();
	}
}
