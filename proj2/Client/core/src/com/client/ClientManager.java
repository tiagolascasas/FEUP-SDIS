package com.client;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import javax.net.ssl.SSLSocket;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.strongjoshua.console.GUIConsole;

public class ClientManager
{
	private static ClientManager instance = null;
	private SSLSocket socket;
	private String username;
	private String password;
	private GUIConsole console;
	private ServerListener listener;
	private boolean loggedIn;
	private boolean connected = false;
	private FileHandle confFile;
	private ArrayList<String> servers;
	private Semaphore drawingMutex;
	private Music currentTrack;
	private CountDownLatch latch;

	private ClientManager()
	{
		this.servers = new ArrayList<>();
		this.drawingMutex = new Semaphore(1);
		this.currentTrack = null;
		this.latch = new CountDownLatch(1);
	}
	
	public synchronized static ClientManager getInstance()
	{
		if (instance == null)
			instance = new ClientManager();
		return instance;
	}

	public synchronized String getPassword()
	{
		return password;
	}

	public synchronized void setPassword(String password)
	{
		this.password = password;
	}

	public synchronized String getUsername()
	{
		return username;
	}

	public synchronized void setUsername(String username)
	{
		this.username = username;
	}

	public synchronized SSLSocket getSocket()
	{
		return socket;
	}

	public synchronized void setSocket(SSLSocket socket)
	{
		this.socket = socket;
	}

	public synchronized GUIConsole getConsole()
	{
		return console;
	}

	public synchronized void setConsole(GUIConsole console)
	{
		this.console = console;
	}

	public synchronized boolean getLoggedIn()
	{
		return this.loggedIn;
	}
	
	public synchronized void initListener()
	{
		this.listener = new ServerListener(this.socket);
		this.listener.start();
	}

	public synchronized void setLoggedIn(boolean isLoggedIn)
	{
		this.loggedIn = isLoggedIn;
	}

	public synchronized void setConnected(boolean b)
	{
		this.connected  = b;
	}

	public synchronized boolean getConnected()
	{
		return this.connected;
	}

	public synchronized void setConfFile(FileHandle confFile)
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
	
	public synchronized FileHandle getConfFile()
	{
		return this.confFile;
	}
	
	public synchronized void log(String s)
	{
		try 
		{
			this.drawingMutex.acquire();
			this.console.log(s);
			this.drawingMutex.release();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}

	public Semaphore getDrawingMutex() 
	{
		return drawingMutex;
	}

	public void setDrawingMutex(Semaphore drawingMutex) 
	{
		this.drawingMutex = drawingMutex;
	}

	public void setActiveTrack(Music track) 
	{
		if (this.currentTrack == null)
			this.currentTrack = track;
		else
		{
			this.currentTrack.stop();
			this.currentTrack = track;
		}
	}

	public ArrayList<String> getServers() 
	{
		return servers;
	}

	public CountDownLatch getConnectedLatch() 
	{
		return this.latch;
	}

	public void stopPlaying() 
	{
		if (this.currentTrack != null && this.currentTrack.isPlaying())
		{
			this.currentTrack.stop();
			log("Stopped playing the current track");
		}
		else
			log("No track is currently being played");
	}
}
