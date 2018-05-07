package com.client.requests;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import com.client.ClientManager;

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
			ClientManager.getInstance().getConsole().log("You are already connected");
			return;
		}
		InetAddress ip = null;
		try
		{
			ip = InetAddress.getByName(this.ip);
		} 
		catch (UnknownHostException e)
		{
			ClientManager.getInstance().getConsole().log("Unable to resolve specified IP " + ip);
			return;
		}
		
		ClientManager.getInstance().getConsole().log("Attempting to connect to " + ip + ":" + port);
		try
		{
			Socket socket = new Socket(ip, this.port);
			ClientManager.getInstance().setSocket(socket);
		} 
		catch (IOException e)
		{
			ClientManager.getInstance().getConsole().log("Error connecting to " + ip + ":" + port);
			return;
		}
		ClientManager.getInstance().getConsole().log("Connection to " + ip + ":" + port + " successfully established!");
		
		ClientManager.getInstance().initListener();
		RequestConnect.isConnected = true;
	}
}
