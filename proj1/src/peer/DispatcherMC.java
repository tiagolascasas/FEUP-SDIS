package peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;

import peer.handler.multicast.StoredHandler;

public class DispatcherMC extends Dispatcher
{
	MulticastSocket socket;
	String version;
	int serverID;
	
	public DispatcherMC(MulticastSocket socket)
	{
		this.socket = socket;
	}
	
	
	@Override
	public void run() 
	{
		while (Peer.running)
		{
			byte[] buffer = new byte[MAX_BUFFER];
			DatagramPacket recPacket = new DatagramPacket(buffer, buffer.length);
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
		if (type.equalsIgnoreCase("STORED"))
		{
			StoredHandler handler = new StoredHandler(message);
			handler.start();
		}
	}
}
