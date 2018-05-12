package com.client;

import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
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
	private FileHandle confFile;
	private Queue<String> servers;

	private ClientManager()
	{
		this.servers = new LinkedList<>();
	}
	
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

	public void setConfFile(FileHandle confFile)
	{
		this.confFile = confFile;
		JsonReader reader = new JsonReader();
		JsonValue val = reader.parse(confFile);
		JsonValue servers = val.get("servers");
		JsonValue str = servers.child;
		while (str != null)
		{
			this.servers.add(str.asString());
			str = str.next();
		}
	}
	
	public FileHandle getConfFile()
	{
		return this.confFile;
	}
	
	public String getNextServer()
	{
		return servers.poll();
	}
}
