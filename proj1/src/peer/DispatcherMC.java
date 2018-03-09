package peer;

import java.net.MulticastSocket;

public class DispatcherMC implements Runnable 
{
	MulticastSocket socket;
	String version;
	int serverID;
	
	public DispatcherMC(MulticastSocket socket, String version, int serverID)
	{
		this.socket = socket;
		this.version = version;
		this.serverID = serverID;
	}
	
	
	@Override
	public void run() 
	{
		
		
	}

}
