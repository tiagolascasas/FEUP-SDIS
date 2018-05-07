package com.client;

import java.net.Socket;

import com.strongjoshua.console.Console;
import com.strongjoshua.console.GUIConsole;

public class ClientManager
{
	private static ClientManager instance = null;
	private Socket socket;
	private String username;
	private String password;
	private GUIConsole console;
	private ServerListener listener;

	private ClientManager(){}
	
	public static ClientManager getInstance()
	{
		if (instance == null)
			instance = new ClientManager();
		return instance;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public Socket getSocket()
	{
		return socket;
	}

	public void setSocket(Socket socket)
	{
		this.socket = socket;
	}

	public GUIConsole getConsole()
	{
		return console;
	}

	public void setConsole(GUIConsole console)
	{
		this.console = console;
	}

	public boolean isLoggedIn()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	public void initListener()
	{
		if (this.listener != null)
		{
			this.listener = new ServerListener(this.socket);
			this.listener.run();
		}
	}
}
