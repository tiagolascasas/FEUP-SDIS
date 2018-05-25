package com.client;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.Gdx;
import com.client.handlers.ResponseHandlerDownload;
import com.client.handlers.ResponseHandlerLogin;
import com.client.handlers.ResponseHandlerNotification;
import com.client.handlers.ResponseHandlerRegister;
import com.client.handlers.ResponseHandlerSearch;
import com.client.handlers.ResponseHandlerUpload;

public class ServerListener extends Thread
{
	private Socket socket;
	private boolean running = true;
	private ThreadPoolExecutor threads;
	
	public ServerListener(Socket socket)
	{
		this.socket = socket;
		this.threads = new ThreadPoolExecutor(
	            200,
	            400,
	            10000,
	            TimeUnit.MILLISECONDS,
	            new LinkedBlockingQueue<Runnable>()
				);
	}

	@Override
	public void run()
	{
		ClientManager.getInstance().log("Subscribed to push notifications");
		while(this.running)
		{
			ArrayList<Byte> message = new ArrayList<Byte>();
			boolean reading = true;
			while(reading)
			{
				int read;
				try
				{
					read = socket.getInputStream().read();
					if (read == 0)
						reading = false;
					else if (read == -1)
						this.running = false;
					else
						message.add((byte)read);
				} 
				catch (IOException e)
				{
					this.running = false;
					break;
				}
			}
			if (!this.running)
				break;
			
			String messageStr = byteToString(message);
			processMessage(messageStr);
		}
		try
		{
			this.socket.close();
			ClientManager.getInstance().log("The server terminated the connection");
			ClientManager.getInstance().setConnected(false);
			ClientManager.getInstance().setLoggedIn(false);
		}
		catch (IOException e)
		{
			System.out.println("Error closing a connection from server " + this.socket.getRemoteSocketAddress());
		}
		return;
	}
	
	public static String byteToString(ArrayList<Byte> message)
	{
		byte[] array = new byte[message.size()];
		for (int i = 0; i < message.size(); i++)
			array[i] = message.get(i);
		return new String(array, StandardCharsets.US_ASCII);
	}

	/**
	 * Message formats:
	 * 
	 * RES_CLOSE <status> <message>
	 * RES_DOWNLOAD <status> [<title> <body>]
	 * RES_LOGIN <status> <message>
	 * RES_REGISTER <status> <message>
	 * RES_SEARCH <status> <results>
	 * RES_UPLOAD <status> <message>
	 * NOTIF <content>
	 * 
	 * @param message
	 */
	protected void processMessage(String message)
	{
		String[] elements = message.split(" ");
		if (elements.length < 2)
		{
			System.out.println("Dropped uncomprehensible message \"" + message + "\"");
			return;
		}
		boolean hasErrors = false;
		
		switch(elements[0])
		{
			case "RES_CLOSE":
			{
				if (elements.length == 3)
				{
					this.running = false;
					Gdx.app.exit();
				}
				else
					hasErrors = true;
				break;
			}
			case "RES_DOWNLOAD":
			{
				if (elements.length == 3)
					threads.execute(new ResponseHandlerDownload(elements[1], elements[2]));
				else
					hasErrors = true;
				break;
			}
			case "RES_LOGIN":
			{
				if (elements.length == 3)
					threads.execute(new ResponseHandlerLogin(elements[1], elements[2]));
				else
					hasErrors = true;
				break;
			}
			case "RES_REGISTER":
			{
				if (elements.length == 3)
					threads.execute(new ResponseHandlerRegister(elements[1], elements[2]));
				else
					hasErrors = true;
				break;
			}
			case "RES_SEARCH":
			{
				if (elements.length == 4)
					(new ResponseHandlerSearch(elements[1], elements[2])).run();
				else
					hasErrors = true;
				break;
			}
			case "RES_UPLOAD":
			{
				if (elements.length == 3)
					(new ResponseHandlerUpload(elements[1], elements[2])).run();
				else
					hasErrors = true;
				break;
			}
			case "NOTIF":
			{
				if (elements.length == 2)
					(new ResponseHandlerNotification(elements[1])).run();
				else
					hasErrors = true;
				break;
			}
			default:
			{
				hasErrors = true;
				break;
			}
		}
		if (hasErrors)
			System.out.println("Dropped uncomprehensible message \"" + message + "\"");
	}
}
