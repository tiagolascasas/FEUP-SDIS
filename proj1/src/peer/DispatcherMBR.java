package peer;

import java.net.MulticastSocket;

public class DispatcherMBR implements Runnable 
{
	MulticastSocket socket;
	String version;
	int serverID;

	public DispatcherMBR(MulticastSocket mbrSocket, String version, int serverID) 
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() 
	{
		// TODO Auto-generated method stub

	}

}
