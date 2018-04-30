package com.client.requests;

import java.io.IOException;
import java.net.Socket;

import com.client.DataManager;

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
		Socket socket = DataManager.getInstance().getSocket();
		try
		{
			socket.getOutputStream().write(message.getBytes());
		} 
		catch (IOException e)
		{
			DataManager.getInstance().getConsole().log("Error sending a request");
		}
	}
	
	protected boolean checkAuthenticated()
	{
		if (!DataManager.getInstance().isLoggedIn())
		{
			DataManager.getInstance().getConsole().log("You cannot use this functionality without authentication");
			return false;
		}
		return true;
	}
	
	protected String getMessageHeader()
	{
		return this.type + " " + DataManager.getInstance().getUsername() + " " + DataManager.getInstance().getPassword();
	}
}
