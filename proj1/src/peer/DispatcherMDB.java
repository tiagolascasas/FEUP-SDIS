package peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.util.Vector;

import peer.handler.Handler;
import peer.handler.multicast.PutchunkHandler;

public class DispatcherMDB extends Dispatcher
{
	MulticastSocket socket;
	Vector<Handler> handlers;
	
	public DispatcherMDB(MulticastSocket mdbSocket) 
	{
		this.socket = mdbSocket;
		this.handlers = new Vector<Handler>();
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
				//System.out.println("MDB: read packet with " + recPacket.getLength() + " bytes");
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
			handlers.add(handler);
			handler.start();
		}
	}
}
