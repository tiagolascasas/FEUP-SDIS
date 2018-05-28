package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSocket;

import server.handlers.HandlerRegister;
import server.handlers.HandlerUpload;

public class LeaderListener 
{
	private boolean running = true;
	private SSLSocket leaderSocket;
	private ThreadPoolExecutor threads;
	
	public LeaderListener(SSLSocket leaderSocket)
	{
		this.leaderSocket = leaderSocket;
		this.threads = new ThreadPoolExecutor(
	            200,
	            400,
	            10000,
	            TimeUnit.MILLISECONDS,
	            new LinkedBlockingQueue<Runnable>()
				);
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
			}
			String messageStr = Utils.byteToString(message);
			String outputMsg = messageStr.length() > 100 ? messageStr.substring(0, 99) : messageStr;
			System.out.println("Received message: " + outputMsg);
			
			processMessage(messageStr);
		}
		return 0;
	}

	private void processMessage(String message) 
	{
		String[] elements = message.split(" ");
		
		if (elements.length < 1)
		{
			System.out.println("Dropped uncomprehensible message \"" + message + "...\"");
			return;
		}
		boolean hasErrors = false;
		
		switch(elements[0])
		{
			case "REGISTER":
			{
				if (elements.length == 3)
					this.threads.execute(new HandlerRegister(null, elements[1], elements[2]));
				else
					hasErrors = true;
				break;
			}
			case "UPLOAD":
			{
				if (elements.length == 5)
					this.threads.execute(new HandlerUpload(null, elements[1], elements[2], elements[3], elements[4]));
				else
					hasErrors = true;
				break;
			}
			case "STATE":
			{
				byte[] state = Utils.decode(elements[1]).getBytes();
				ServerManager.getInstance().setState(state);
				break;
			}
			default:
			{
				hasErrors = true;
				break;
			}
		}
		if (hasErrors)
			System.out.println("Dropped uncomprehensible message \"" + message + "...\"");
	}
}
