package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class Advertiser extends Thread 
{
	MulticastSocket socket;
	DatagramPacket packet;
	
	public Advertiser(MulticastSocket socket, DatagramPacket packet)
	{
		this.socket = socket;
		this.packet = packet;
	}
	
	public void run()
	{
		try
		{
			socket.setTimeToLive(1);
			while(true)
			{
				//System.out.println("Sending multicast advertisement");
				socket.send(packet);
				Thread.sleep(1000);
			}
		} 
		catch (Exception e) {};
			
    }
}
