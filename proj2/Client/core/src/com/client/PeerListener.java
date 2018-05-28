package com.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import com.client.handlers.ResponseHandlerDownload;

public class PeerListener implements Runnable
{
	private int port;
	private ServerSocket socket;
	
	public PeerListener()
	{
		try 
		{
			this.socket = new ServerSocket(0);
			this.port = socket.getLocalPort();
			ClientManager.getInstance().setP2PPort(this.port);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	public void run() 
	{
		while (true)
		{
			try 
			{
				Socket peer = this.socket.accept();
				ClientManager.getInstance().log("Receiving the track from a client instead");
				ArrayList<Byte> message = new ArrayList<Byte>();
				boolean reading = true;
				while(reading)
				{
					int read;
					try
					{
						read = peer.getInputStream().read();
						if (read == 0)
							reading = false;
						else
							message.add((byte)read);
					} 
					catch (IOException e)
					{
						break;
					}
				}
				processMessage(ServerListener.byteToString(message));
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	private void processMessage(String message)
	{
		String[] elements = message.split(" ");
		String title = Utils.decode(elements[1]);
		String body = Utils.decode(elements[2]);
		
		System.out.println("title = " + title);
		System.out.println("body size = "  + body.length());
		
        String tempDir = System.getProperty("java.io.tmpdir");
		String path = tempDir + "/" + title;
		
		ResponseHandlerDownload.save(title, body.getBytes(), path);
		ClientManager.getInstance().log("Now playing track " + title);
		ResponseHandlerDownload.play(title, path);
	}
}
