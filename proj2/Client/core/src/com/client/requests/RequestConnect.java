package com.client.requests;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import com.client.ClientManager;


public class RequestConnect extends Request
{
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
		if (ClientManager.getInstance().getConnected() == true)
		{
			ClientManager.getInstance().log("You are already connected");
			return;
		}
		InetAddress ip = null;
		try
		{
			ip = InetAddress.getByName(this.ip);
		} 
		catch (UnknownHostException e)
		{
			ClientManager.getInstance().log("Unable to resolve specified IP " + ip);
			return;
		}
		
		System.setProperty("javax.net.ssl.trustStore", "truststore");
		System.setProperty("javax.net.ssl.keyStorePassword", "123456");
		
		ClientManager.getInstance().log("Attempting to connect to " + ip + ":" + port);
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
			ClientManager.getInstance().log("Error connecting to " + ip + ":" + port);
			return;
		}
		
		ClientManager.getInstance().log("Connection to " + ip + ":" + port + " successfully established!");
		
		ClientManager.getInstance().initListener();
		ClientManager.getInstance().setConnected(true);
	}
}
