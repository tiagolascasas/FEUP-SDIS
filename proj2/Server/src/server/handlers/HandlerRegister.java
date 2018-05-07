package server.handlers;

import java.io.IOException;
import java.net.Socket;
import server.ServerManager;
import server.utils.Utils;

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
		
		try
		{
			log("sending message \"" + s + "\"");
			socket.getOutputStream().write(s.getBytes());
		} 
		catch (IOException e)
		{
			log("Unable to write response to client " + socket.getRemoteSocketAddress());
		}
		log("Exiting");
	}
}
