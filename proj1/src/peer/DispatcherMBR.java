package peer;

import java.net.MulticastSocket;

public class DispatcherMBR extends Dispatcher
{
	MulticastSocket socket;
	String version;
	int serverID;

	public DispatcherMBR(MulticastSocket mbrSocket) 
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() 
	{
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
