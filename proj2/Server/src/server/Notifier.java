package server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Notifier extends Thread
{
	private String excludeClient;
	private String message;

	public Notifier(String excludeClient, String message)
	{
		this.excludeClient = excludeClient;
		this.message = message;
	}

	@Override
	public void run()
	{
		String fullMsg = "NOTIF " + Utils.encode(message) + "\0";
		
		ArrayList<Socket> sockets = ServerManager.getInstance().getOnlineSockets(excludeClient);
		for (Socket socket : sockets)
		{
			try
			{
				socket.getOutputStream().write(fullMsg.getBytes());
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

}
