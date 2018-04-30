package com.client.requests;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.client.DataManager;

public class RequestConnect extends Request
{
	public static boolean isConnected = false;
	private String ip;
	private int port;
	
	public RequestConnect(String ip, int port)
	{
		super("CONNECT");
		this.ip = ip;
		this.port = port;
	}
	
	@Override
	public void run()
	{
		if (isConnected == true)
		{
			DataManager.getInstance().getConsole().log("You are already connected");
			return;
		}
		InetAddress ip = null;
		try
		{
			ip = InetAddress.getByName(this.ip);
		} 
		catch (UnknownHostException e)
		{
			DataManager.getInstance().getConsole().log("Unable to resolve specified IP " + ip);
			return;
		}
		
		DataManager.getInstance().getConsole().log("Attempting to connect to " + ip + ":" + port);
		try
		{
			Socket socket = new Socket(ip, this.port);
			DataManager.getInstance().setSocket(socket);
		} 
		catch (IOException e)
		{
			DataManager.getInstance().getConsole().log("Error connecting to " + ip + ":" + port);
			return;
		}
		DataManager.getInstance().getConsole().log("Connection to " + ip + ":" + port + " successfully established!");
		this.isConnected = true;
	}
}
