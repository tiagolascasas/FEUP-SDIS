package peer;

import java.net.MulticastSocket;

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
		System.out.println("MC Dispatcher");
		while (Peer.running)
		{

		}
	}


	@Override
	void processMessage(byte[] message)
	{
		// TODO Auto-generated method stub
		
	}

}
