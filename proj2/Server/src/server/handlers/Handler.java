package server.handlers;

import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import server.ServerManager;
import server.Utils;

public abstract class Handler implements Runnable
{
	protected String type;
	protected String username;
	protected String password;
	protected Socket socket;
	private Calendar cal;

	public Handler(String type, String username, String password, Socket socket)
	{
		this.type = type;
		this.username = username;
		this.password = password;
		this.socket = socket;
		this.cal = Calendar.getInstance();
	}

	@Override
	public abstract void run();
	
	protected void log(String s)
	{
		String out = "Client " + socket.getRemoteSocketAddress();
		out += " | " + type;
		out += " | ";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        out += sdf.format(cal.getTime());
        out +=" | " + s;
		
        ServerManager.getInstance().log(out);
	}
	
	protected void send(String message)
	{
		try
		{
			log("Sending message \"" + message + "\"");
			socket.getOutputStream().write(message.getBytes());
		} 
		catch (IOException e)
		{
			log("Unable to write response to client");
		}
	}
	
	protected boolean verifyCredentials()
	{
		String hash = Utils.hashPassword(password);
		
		if (!ServerManager.getInstance().userIsRegistered(username))
			return false;
		else return ServerManager.getInstance().passwordIsCorrect(username, hash);
	}
}
