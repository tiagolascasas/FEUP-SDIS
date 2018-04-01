package peer.handler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

import peer.Channels;
import peer.Manager;

public abstract class Handler extends Thread
{
	protected String handlerType;
	
	public static void send(Channels channel, byte[] message)
	{
		DatagramPacket packet = new DatagramPacket(
							message,
							message.length,
							Manager.getInstance().getAddress(channel),
							Manager.getInstance().getPort(channel));
		MulticastSocket socket = Manager.getInstance().getSocket(channel);
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
