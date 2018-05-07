package server.handlers;

import java.net.Socket;

public class HandlerUpload extends Handler
{

	public HandlerUpload(Socket socket, String username, String password, String title, String body)
	{
		super("UPLOAD", username, password, socket);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub

	}

}
