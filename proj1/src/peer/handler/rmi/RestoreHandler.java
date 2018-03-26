package peer.handler.rmi;

import peer.CHANNELS;
import peer.handler.Handler;
import peer.message.MessageGetchunk;

public class RestoreHandler extends Handler
{
	private String fileId;
	private int numberOfChunks;

	public RestoreHandler(String fileId, int numberOfChunks)
	{
		this.fileId = fileId;
		this.numberOfChunks = numberOfChunks;
	}
	
	@Override
	public void run()
	{
		for (int i = 0; i < this.numberOfChunks; i++)
		{
			MessageGetchunk message = new MessageGetchunk(fileId.getBytes(), i);
			send(CHANNELS.MC, message.getMessageBytes());
		}
	}
}
