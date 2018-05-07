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
		ServerManager.getInstance().registerUser(username, hash);
		
		String response = "RES_REGISTER 1 OK\0";
		try
		{
			socket.getOutputStream().write(response.getBytes());
		} 
		catch (IOException e)
		{
			log("Unable to write response to client " + socket.getRemoteSocketAddress());
		}
		log("Exiting");
	}
}
