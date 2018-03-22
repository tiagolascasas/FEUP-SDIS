package peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import peer.handler.multicast.PutchunkHandler;

public class DispatcherMDB extends Dispatcher
{
	private static final int MAX_BUFFER = 65000;
	MulticastSocket socket;
	
	public DispatcherMDB(MulticastSocket mdbSocket) 
	{
		this.socket = mdbSocket;
	}

	@Override
	public void run() 
	{
		System.out.println("MDB Dispatcher");
		while (Peer.running)
		{
			byte[] buffer = new byte[MAX_BUFFER];
			DatagramPacket recPacket = new DatagramPacket(buffer, 256);
			try
			{
				socket.receive(recPacket);
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
			processMessage(recPacket.getData());
		}

	}

	@Override
	void processMessage(byte[] message)
	{
		String type = new String(message, StandardCharsets.US_ASCII).split(" ")[0];
		if (type.equalsIgnoreCase("PUTCHUNK"))
		{
			PutchunkHandler handler = new PutchunkHandler(message);
			handler.start();
		}
		
	}

}
