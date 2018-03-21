package peer.handler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

import peer.CHANNELS;
import peer.DataManager;

public abstract class Handler extends Thread
{
	public void send(CHANNELS channel, byte[] message)
	{
		DatagramPacket packet = new DatagramPacket(
							message,
							message.length,
							DataManager.getInstance().getAddress(channel),
							DataManager.getInstance().getPort(channel));
		MulticastSocket socket = DataManager.getInstance().getSocket(channel);
		try
		{
			socket.send(packet);
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
