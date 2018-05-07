package com.client;

import java.net.Socket;

public class ServerListener implements Runnable 
{
	private Socket socket;
	
	public ServerListener(Socket socket)
	{
		this.socket = socket;
	}
	
	@Override
	public void run()
	{
		// TODO Auto-generated method stub

	}
}
