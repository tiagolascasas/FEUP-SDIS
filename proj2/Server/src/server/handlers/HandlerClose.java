package server.handlers;

import java.io.IOException;
import java.net.Socket;

import server.Notifier;
import server.ServerManager;
import server.utils.Utils;

public class HandlerClose extends Handler
{

	public HandlerClose(Socket socket, String username, String password)
	{
		super("CLOSE", username, password, socket);
	}

	@Override
	public void run()
	{
		log("Starting");
		ServerManager.getInstance().logoutUser(username);
		
		String res = "RES_CLOSE 1 " + Utils.encode("Successfully ended the session") + '\0';
		send(res);
		try
		{
			socket.close();
		} 
		catch (IOException e)
		{
			System.out.println("Error closing connection to client " + socket.getRemoteSocketAddress());
		}
		
		String notify = "Client " + username + " is now offline";
		Notifier notif = new Notifier(username, notify);
		notif.start();
		
		log("Exiting");
	}

}
