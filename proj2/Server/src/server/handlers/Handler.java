package server.handlers;

import java.net.Socket;

public abstract class Handler implements Runnable
{
	protected String type;
	protected String username;
	protected String password;
	protected Socket socket;

	public Handler(String type, String username, String password, Socket socket)
	{
		this.type = type;
		this.username = username;
		this.password = password;
		this.socket = socket;
	}

	@Override
	public abstract void run();
}
