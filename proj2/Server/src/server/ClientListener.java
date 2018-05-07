package server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import server.handlers.HandlerClose;
import server.handlers.HandlerDownload;
import server.handlers.HandlerLogin;
import server.handlers.HandlerRegister;
import server.handlers.HandlerSearch;
import server.handlers.HandlerUpload;
import server.utils.Utils;

public class ClientListener implements Runnable
{
	private Socket socket;
	private boolean running = true;

	public ClientListener(Socket socket)
	{
		this.socket = socket;
	}

	@Override
	public void run()
	{
		while(this.running)
		{
			ArrayList<Byte> message = new ArrayList<>();
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
					e.printStackTrace();
				}
			}
			String messageStr = Utils.byteToString(message);
			System.out.println("Received message: " + messageStr);
			processMessage(messageStr);
		}
		try
		{
			this.socket.close();
			System.out.println("Closed socket of client " + this.socket.getRemoteSocketAddress());
		} 
		catch (IOException e)
		{
			System.out.println("Error closing socket of client " + this.socket.getRemoteSocketAddress());
		}
		return;
	}

	/**
	 * Message formats:
	 * 
	 * CLOSE <username> <password>
	 * DOWNLOAD <username> <password> <title>
	 * LOGIN <username> <password>
	 * REGISTER <username> <password>
	 * SEARCH <username> <password> <search term>
	 * UPLOAD <username> <password> <title> <body>
	 * 
	 * @param message
	 */
	private void processMessage(String message)
	{
		String[] elements = message.split(" ");
		if (elements.length < 3)
		{
			System.out.println("Dropped uncomprehensible message \"" + message + "\"");
			return;
		}
		boolean hasErrors = false;
		
		switch(elements[0])
		{
			case "CLOSE":
			{
				if (elements.length == 3)
					(new HandlerClose(socket, elements[1], elements[2])).run();
				else
					hasErrors = true;
				break;
			}
			case "DOWNLOAD":
			{
				if (elements.length == 4)
					(new HandlerDownload(socket, elements[1], elements[2])).run();
				else
					hasErrors = true;
				break;
			}
			case "LOGIN":
			{
				if (elements.length == 3)
					(new HandlerLogin(socket, elements[1], elements[2])).run();
				else
					hasErrors = true;
				break;
			}
			case "REGISTER":
			{
				if (elements.length == 3)
					(new HandlerRegister(socket, elements[1], elements[2])).run();
				else
					hasErrors = true;
				break;
			}
			case "SEARCH":
			{
				if (elements.length == 4)
					(new HandlerSearch(socket, elements[1], elements[2], elements[3])).run();
				else
					hasErrors = true;
				break;
			}
			case "UPLOAD":
			{
				if (elements.length == 5)
					(new HandlerUpload(socket, elements[1], elements[2], elements[3], elements[4])).run();
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
