package peer;

import peer.handler.rmi.StoreHandler;

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
		
		//TEST
		processMessage(null);
		//TEST
		
		while (Peer.running)
		{
			//read something from RMI
			//processMessage(message);
		}

	}

	@Override
	void processMessage(byte[] message)
	{
		byte[] file = TestClass.testPutchunk();
		byte[] metadata = "abcd".getBytes();
		
		if (DataManager.getInstance().getId() == 1)
		{
			StoreHandler handler = new StoreHandler(file, metadata, 2);
			handler.start();
		}
	}

}
