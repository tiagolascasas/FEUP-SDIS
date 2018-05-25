package server.handlers;

import java.net.Socket;
import server.ServerManager;
import server.Utils;

public class HandlerRegister extends Handler 
{
	public HandlerRegister(Socket socket, String username, String password) 
	{
		super("REGISTER", username, password, socket);
	}

	@Override
	public void run() 
	{
		log("Starting");
		String hash = Utils.hashPassword(password);
		boolean ok = ServerManager.getInstance().registerUser(username, hash);
		
		if (this.socket == null)
		{
			log("Backed up user " + username + " with success code " + ok);
			return;
		}
		
		StringBuilder build = new StringBuilder();
		build.append("RES_REGISTER ");
		if (ok)
		{
			String message = Utils.encode("Successfully registered the new user");
			build.append(1).append(" ").append(message);
		}
		else
		{
			String message = Utils.encode("Error: username already exists");
			build.append(0).append(" ").append(message);
		}
		build.append('\0');
		String s = build.toString();
		send(s);
		log("Exiting");
	}
}
