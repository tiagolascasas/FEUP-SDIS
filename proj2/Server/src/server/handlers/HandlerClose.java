package server.handlers;

import java.net.Socket;

public class HandlerClose extends Handler
{

	public HandlerClose(Socket socket, String username, String password)
	{
		super("CLOSE", username, password, socket);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub

	}

}
