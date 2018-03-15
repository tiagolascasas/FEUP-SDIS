package peer;

import java.net.MulticastSocket;

public class DispatcherMDB extends Dispatcher
{
	MulticastSocket socket;
	String version;
	int serverID;
	
	public DispatcherMDB(MulticastSocket mdbSocket) 
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() 
	{
		System.out.println("MDB Dispatcher");
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
