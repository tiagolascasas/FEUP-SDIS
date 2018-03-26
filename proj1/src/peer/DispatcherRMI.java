package peer;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import peer.handler.rmi.BackupHandler;
import peer.handler.rmi.RestoreHandler;

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
		//TESTS ONLY RUN ON PEER 1
		if (Manager.getInstance().getId() == 1)
		{
			//TEST BACKUP
			byte[] file = Utilities.fileToBinary("../scripts/test3.pdf");
			byte[] metadata = Utilities.calculateMetadataIdentifier("../scripts/test3.pdf");
			
			threads.execute(new BackupHandler(file, metadata, 2));
			
			//Sleep a bit until backup is done
			try{Thread.sleep(9000);} catch (InterruptedException e){e.printStackTrace();}
			
			//TEST RESTORE
			int numberOfChunks = Utilities.calculateNumberOfChunks(file);
			String fileId = Utilities.calculateFileId(metadata, file);
			
			threads.execute(new RestoreHandler(fileId, 23));
			
			RestoreManager man = Manager.getInstance().getRestoredManager();
			while(!man.isComplete(fileId)) {}
			Utilities.binaryToFile(man.reassemble(fileId), "test3.pdf");
			
			//Sleep a bit until restore is done
			try{Thread.sleep(5000);} catch (InterruptedException e){e.printStackTrace();}
			
			//TEST DELETE
			
			//TEST SPACE RECLAIMING
			
			//TEST STATE
		}
	}
}
