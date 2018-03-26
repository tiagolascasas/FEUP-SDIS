package peer.handler.rmi;

import peer.Channels;
import peer.Manager;
import peer.RestoreManager;
import peer.handler.Handler;
import peer.message.MessageGetchunk;

public class RestoreHandler extends Handler
{
	private String fileId;
	private int numberOfChunks;

	public RestoreHandler(String fileId, int numberOfChunks)
	{
		this.handlerType = "RestoreHandler";
		this.fileId = fileId;
		this.numberOfChunks = numberOfChunks;
	}
	
	@Override
	public void run()
	{
		log("running restore handler for file with id " + this.fileId);
	
		RestoreManager manager = Manager.getInstance().getRestoredManager();
		manager.createNewEntry(fileId, numberOfChunks);
		
		for (int i = 0; i < this.numberOfChunks; i++)
		{
			MessageGetchunk message = new MessageGetchunk(fileId.getBytes(), i);
			send(Channels.MC, message.getMessageBytes());
		}
	}
}
