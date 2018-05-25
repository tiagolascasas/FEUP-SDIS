package server.handlers;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import server.Utils;

public class LeaderListener 
{
	private boolean running = true;
	private Socket leaderSocket;

	public LeaderListener(Socket leaderSocket)
	{
		this.leaderSocket = leaderSocket;
	}
	
	public int listen() 
	{
		while(this.running)
		{
			boolean reading = true;
			ArrayList<Byte> message = new ArrayList<>();
			
			while(reading)
			{
				int read;
				try
				{
					read = leaderSocket.getInputStream().read();
					if (read == 0)
						reading = false;
					else if (read == -1)
						this.running = false;
					else
						message.add((byte)read);
				} 
				catch (IOException e)
				{
					this.running = false;
					break;
				}
				
				String messageStr = Utils.byteToString(message);
				String outputMsg = messageStr.length() > 100 ? messageStr.substring(0, 99) : messageStr;
				System.out.println("Received message: " + outputMsg);
				
				processMessage(messageStr);
			}
		}
		return 0;
	}

	private void processMessage(String messageStr) 
	{

	}
}
