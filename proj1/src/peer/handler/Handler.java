package peer.handler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

import peer.CHANNELS;
import peer.DataManager;

public abstract class Handler extends Thread
{
	protected String handlerType;
	
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
	
	public void log(String message)
	{
		System.out.println(this.getId() + "-" + this.handlerType + ": " + message);
	}
}
