package com.client.requests;

import java.io.IOException;
import java.net.Socket;

import com.client.ClientManager;

public abstract class Request implements Runnable
{
	protected String type;

	public Request(String type)
	{
		this.type = type;
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub

	}
	
	protected void send(String message)
	{
		if (!ClientManager.getInstance().getConnected())
		{
			ClientManager.getInstance().log("You must be connected to a server in order to send requests");
		}
		
		Socket socket = ClientManager.getInstance().getSocket();
		try
		{
			socket.getOutputStream().write(message.getBytes());
		} 
		catch (IOException e)
		{
			ClientManager.getInstance().log("Error sending a request");
		}
	}
	
	protected boolean checkAuthenticated()
	{
		if (!ClientManager.getInstance().getLoggedIn())
		{
			ClientManager.getInstance().log("You cannot use this functionality without authentication");
			return false;
		}
		return true;
	}
	
	protected String getMessageHeader()
	{
		return this.type + " " + ClientManager.getInstance().getUsername() + " " + ClientManager.getInstance().getPassword();
	}
}
