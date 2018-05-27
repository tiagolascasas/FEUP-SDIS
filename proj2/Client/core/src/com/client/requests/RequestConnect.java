package com.client.requests;

import java.io.IOException;
import java.util.ArrayList;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import com.client.ClientManager;


public class RequestConnect extends Request
{
	private ArrayList<String> servers;
	int currentServer = 0;
	private boolean keepConnecting = true;
	
	public RequestConnect()
	{
		super("CONNECT");
		this.servers = ClientManager.getInstance().getServers();
	}
	
	@Override
	public void run()
	{
		ClientManager.getInstance().log("Attempting to find a server...");
		
		while(this.keepConnecting)
		{
			String server = getNextServer();
			String[] elements = server.split(":");
			int port = Integer.parseInt(elements[1]);
			
			connect(elements[0], port);
			
			while (ClientManager.getInstance().getConnected()) {}
		}
	}
	
	private void connect(String ip, int port)
	{		
		System.setProperty("javax.net.ssl.trustStore", "truststore");
		System.setProperty("javax.net.ssl.keyStorePassword", "123456");
		
		try
		{
			SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();  
			SSLSocket socket = (SSLSocket) ssf.createSocket(ip, port);
			socket.startHandshake();
			//Socket socket = new Socket(ip, this.port); //TODO ssl socket
			ClientManager.getInstance().setSocket(socket);
		}
		catch (IOException e)
		{
			return;
		}
		
		ClientManager.getInstance().log("Connection to " + ip + ":" + port + " successfully established!");
		
		ClientManager.getInstance().initListener();
		ClientManager.getInstance().setConnected(true);
	}
	
	private String getNextServer()
	{
		if (this.currentServer == this.servers.size())
			this.currentServer = 0;
		String server = this.servers.get(this.currentServer);
		this.currentServer++;
		return server;
	}
}
