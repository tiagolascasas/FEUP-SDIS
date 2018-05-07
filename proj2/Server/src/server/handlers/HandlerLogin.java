package server.handlers;

import java.net.Socket;

public class HandlerLogin extends Handler
{

	public HandlerLogin(Socket socket, String username, String password)
	{
		super("LOGIN", username, password, socket);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub

	}

}
