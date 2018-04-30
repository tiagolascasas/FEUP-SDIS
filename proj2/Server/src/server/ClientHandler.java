package server;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable
{
	private Socket socket;
	private boolean loggedIn = false;
	private boolean running = true;

	public ClientHandler(Socket socket)
	{
		this.socket = socket;
	}

	@Override
	public void run()
	{
		while(this.running)
		{
			ArrayList<Byte> message = new ArrayList<>();
			boolean reading = true;
			while(reading)
			{
				int read;
				try
				{
					read = socket.getInputStream().read();
					if (read == 0)
						reading = false;
					else if (read == -1)
						this.running = false;
					else
						message.add((byte)read);
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			String messageStr = Utils.byteToString(message);
			System.out.println("Received message: " + messageStr);
		}
		try
		{
			this.socket.close();
			System.out.println("Closed socket of client " + this.socket.getRemoteSocketAddress());
		} 
		catch (IOException e)
		{
			System.out.println("Error closing socket of client " + this.socket.getRemoteSocketAddress());
		}
		return;
	}

}
