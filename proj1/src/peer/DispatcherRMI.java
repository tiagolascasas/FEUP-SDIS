package peer;

public class DispatcherRMI extends Dispatcher
{
	private String rmiMethodName;
	
	public DispatcherRMI(String rmiMethodName)
	{
		this.rmiMethodName = rmiMethodName;
	}

	@Override
	public void run() 
	{
		System.out.println("RMI Dispatcher");
		while (Peer.running)
		{
			//read something from RMI
		}

	}

	@Override
	void processMessage(byte[] message)
	{
		// TODO Auto-generated method stub
		
	}

}
