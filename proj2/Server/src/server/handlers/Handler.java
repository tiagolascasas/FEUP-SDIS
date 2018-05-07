package server.handlers;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
		
		System.out.println(out);
	}
}
