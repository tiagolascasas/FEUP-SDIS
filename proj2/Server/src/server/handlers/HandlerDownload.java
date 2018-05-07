package server.handlers;

import java.net.Socket;

public class HandlerDownload extends Handler
{

	public HandlerDownload(Socket socket, String username, String password)
	{
		super("DOWNLOAD", username, password, socket);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub

	}

}
