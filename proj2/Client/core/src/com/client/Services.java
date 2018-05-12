package com.client;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.files.FileHandle;
import com.client.requests.RequestClose;
import com.client.requests.RequestConnect;
import com.client.requests.RequestDownload;
import com.client.requests.RequestLogin;
import com.client.requests.RequestRegister;
import com.client.requests.RequestSearch;
import com.client.requests.RequestUpload;
import com.strongjoshua.console.CommandExecutor;
import com.strongjoshua.console.GUIConsole;
import com.strongjoshua.console.annotation.ConsoleDoc;

public class Services extends CommandExecutor 
{
	private ThreadPoolExecutor threads;
	
	public Services(GUIConsole console, FileHandle serverList)
	{
		ClientManager.getInstance().setConsole(console);
		ClientManager.getInstance().setConfFile(serverList);
		
		this.threads = new ThreadPoolExecutor(
	            200,
	            400,
	            10000,
	            TimeUnit.MILLISECONDS,
	            new LinkedBlockingQueue<Runnable>()
				);
		
		String[] serverAddr = ClientManager.getInstance().getNextServer().split(":");
		connect(serverAddr[0], Integer.parseInt(serverAddr[1]));
	}
	
	@ConsoleDoc(description = "Establishes a connection to the server")
	public void connect(String ip, int port)
	{
		RequestConnect request = new RequestConnect(ip, port);
		threads.execute(request);
	}
	
	@ConsoleDoc(description = "Registers the user")
	public void register(String username, String password)
	{
		RequestRegister request = new RequestRegister(username, password);
		threads.execute(request);
	}
	
	@ConsoleDoc(description = "Logs the user onto the server")
	public void login(String username, String password)
	{
		RequestLogin request = new RequestLogin(username, password);
		threads.execute(request);
	}
	
	@ConsoleDoc(description = "Uploads a track to the server")
	public void upload(String track)
	{
		RequestUpload request = new RequestUpload(track);
		threads.execute(request);
	}
	
	@ConsoleDoc(description = "Downloads and plays the specified track")
	public void play(String track)
	{
		RequestDownload request = new RequestDownload(track);
		threads.execute(request);
	}
	
	@ConsoleDoc(description = "Searches for the specified track")
	public void search(String title)
	{
		RequestSearch request = new RequestSearch(title);
		threads.execute(request);
	}
	
	@ConsoleDoc(description = "Closes the application")
	public void exit()
	{
		RequestClose request = new RequestClose();
		threads.execute(request);
	}
	
	@ConsoleDoc(description = "Closes the application (same as exit)")
	public void close()
	{
		exit();
	}

	public ThreadPoolExecutor getThreads()
	{
		return threads;
	}

	public void setThreads(ThreadPoolExecutor threads)
	{
		this.threads = threads;
	}
}
