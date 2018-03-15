package peer;

import java.net.MulticastSocket;

public class DispatcherMC implements Runnable 
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
		
		
	}

}
