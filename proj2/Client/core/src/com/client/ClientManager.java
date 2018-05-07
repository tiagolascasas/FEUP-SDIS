package com.client;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
	private boolean loggedIn;
	private boolean connected = false;

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

	public boolean getLoggedIn()
	{
		return this.loggedIn;
	}
	
	public void initListener()
	{
		this.listener = new ServerListener(this.socket);
		this.listener.start();
	}

	public void setLoggedIn(boolean isLoggedIn)
	{
		this.loggedIn = isLoggedIn;
	}

	public void setConnected(boolean b)
	{
		this.connected  = b;
	}

	public boolean getConnected()
	{
		return this.connected;
	}
}
