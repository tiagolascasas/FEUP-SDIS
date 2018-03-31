package peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class Dispatcher extends Thread
{
	protected static final int MAX_BUFFER = 65000;
	protected ThreadPoolExecutor threads;
	protected MulticastSocket socket;

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
				int len = recPacket.getLength();
				byte[] received = new byte[len];
				System.arraycopy(recPacket.getData(), 0, received, 0, len);
				processMessage(received);
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	abstract void processMessage(byte[] message);
}
