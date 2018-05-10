package server.handlers;

import java.net.Socket;

import server.ServerManager;
import server.utils.Utils;

public class HandlerLogin extends Handler
{
	public HandlerLogin(Socket socket, String username, String password)
	{
		super("LOGIN", username, password, socket);
	}

	@Override
	public void run()
	{
		log("Starting");
		
		ServerManager manager = ServerManager.getInstance();
		
		String hash = Utils.hashPassword(password);
		
		StringBuilder build = new StringBuilder();
		build.append("RES_LOGIN ");
		if (!manager.userIsRegistered(username))
		{
			String message = Utils.encode("Error logging in: this user does not exist");
			build.append(0).append(" ").append(message);
		}
		else if (!manager.passwordIsCorrect(username, hash))
		{
			String message = Utils.encode("Error: the password is incorrect");
			build.append(0).append(" ").append(message);
		}
		else if (!manager.loginUser(username, password, socket))
		{
			String message = Utils.encode("Error: you are already logged in");
			build.append(0).append(" ").append(message);
		}
		else
		{
			String message = Utils.encode("Successfully logged in");
			build.append(1).append(" ").append(message);
		}
		build.append('\0');
		String s = build.toString();
		send(s);
		
		log("Exiting");
	}
}
