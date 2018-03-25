package peer;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import peer.handler.rmi.StoreHandler;

public class DispatcherRMI extends Dispatcher
{
	private String rmiMethodName;
	
	public DispatcherRMI(String rmiMethodName)
	{
		this.rmiMethodName = rmiMethodName;
		this.threads = new ThreadPoolExecutor(
	            4,
	            400,
	            10000,
	            TimeUnit.MILLISECONDS,
	            new LinkedBlockingQueue<Runnable>()
	            );
	}

	@Override
	public void run() 
	{
		
		//TEST
		test();
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
		//executa um rmi handler consoante o tipo de pedido
		//Deletion, Reclamation, Restore ou Store
	}
	
	void test()
	{
		byte[] file = TestClass.testPutchunk();
		byte[] metadata = "abcd".getBytes();
		
		if (DataManager.getInstance().getId() == 1)
		{
			threads.execute(new StoreHandler(file, metadata, 2));
		}
	}
}
