package com.client.requests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

		try
		{
			SSLSocketFactory ssf = (SSLSocketFactory) SSLSocketFactory.getDefault();
			SSLSocket socket = (SSLSocket) ssf.createSocket(ip, port);
			socket.startHandshake();
			ClientManager.getInstance().setSocket(socket);
		}
		catch (IOException e)
		{
			return;
		}

		ClientManager.getInstance().log("Connection to " + ip + ":" + port + " successfully established!");

		ClientManager.getInstance().initListener();
		ClientManager.getInstance().setConnected(true);

		if (ClientManager.getInstance().getPassword() != null)
		{
			ExecutorService es = Executors.newCachedThreadPool();
			es.execute(new RequestLogin(ClientManager.getInstance().getUsername(),
										ClientManager.getInstance().getPassword()));
		}
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
