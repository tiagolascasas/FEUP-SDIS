package server.handlers;

import java.net.Socket;

public class HandlerSearch extends Handler
{
	public HandlerSearch(Socket socket, String username, String password, String searchTerm)
	{
		super("SEARCH", username, password, socket);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub

	}

}
