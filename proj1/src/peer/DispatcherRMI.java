package peer;

import java.nio.charset.StandardCharsets;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Base64;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import peer.handler.rmi.BackupHandler;
import peer.handler.rmi.DeletionHandler;
import peer.handler.rmi.RestoreHandler;
import peer.message.MessageRMI;

public class DispatcherRMI extends Dispatcher implements MessageRMI
{
	private String rmiMethodName;

	private byte[] filecontent = null;

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


		//read something from RMI
		//processMessage(message);
		try {
			MessageRMI stub = (MessageRMI) UnicastRemoteObject.exportObject(this, 0);

			// Bind the remote object's stub in the registry
			Registry registry = LocateRegistry.getRegistry();
			registry.bind(this.rmiMethodName, stub);
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("An error occured, couldn't start server...");
		}

	}

	public byte[] sendMessage(String operation, String[] args, byte[][] file) {
		String msg = operation;
		byte[] message;
		for(int i = 0; i<args.length;i++)
		{
			msg += " " + args[i];
		}
		if(file[0] != null)
		{
			message = (msg + " " + 
					new String(file[0]) +
					" " + new String(file[1])).getBytes();
		}
		else
		{
			message = msg.getBytes(); 
		}
		this.processMessage(message);

		return this.filecontent; //returns file to client if restore or null for another option
	}

	@Override
	void processMessage(byte[] message)
	{
		String[] msg = new String(message, StandardCharsets.US_ASCII).split(" ");
		String operation = msg[0];

		//executa um rmi handler consoante o tipo de pedido
		//Deletion, Reclamation, Restore ou Store
		switch(operation)
		{
		case "BACKUP":
			threads.execute(new BackupHandler(Base64.getDecoder().decode(msg[2].getBytes()), Base64.getDecoder().decode(msg[3].getBytes()), Integer.parseInt(msg[1])));
			break;
		case "RESTORE":
			int numberOfChunks = Utilities.calculateNumberOfChunks(Base64.getDecoder().decode(msg[1].getBytes()));
			String fileId = Utilities.calculateFileId(Base64.getDecoder().decode(msg[2].getBytes()), Base64.getDecoder().decode(msg[1].getBytes()));
			threads.execute(new RestoreHandler(fileId, numberOfChunks));
			RestoreManager man = Manager.getInstance().getRestoredManager();
			while(!man.isComplete(fileId)) {}
			this.filecontent = man.reassemble(fileId);
			break;
		case "DELETE":
			//TODO call delete method
			break;
		case "RECLAIM":
			//TODO call reclaim method
			break;
		case "STATE":
			//TODO call state method
			break;			
		}
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
			try{Thread.sleep(12000);} catch (InterruptedException e){e.printStackTrace();}

			//TEST RESTORE
			int numberOfChunks = Utilities.calculateNumberOfChunks(file);
			String fileId = Utilities.calculateFileId(metadata, file);
			/*
			threads.execute(new RestoreHandler(fileId, numberOfChunks));

			RestoreManager man = Manager.getInstance().getRestoredManager();
			while(!man.isComplete(fileId)) {}
			Utilities.binaryToFile(man.reassemble(fileId), "test1.txt");

			//Sleep a bit until restore is done
			try{Thread.sleep(5000);} catch (InterruptedException e){e.printStackTrace();}*/

			//TEST DELETE
			//threads.execute(new DeletionHandler(fileId));
			
			//TEST SPACE RECLAIMING

			//TEST STATE
			
		}
		//DO NOTHING MORE
		while(true) {}
	}
}
