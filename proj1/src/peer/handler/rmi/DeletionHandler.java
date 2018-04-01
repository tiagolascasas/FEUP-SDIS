package peer.handler.rmi;

import peer.Channels;
import peer.Manager;
import peer.Utilities;
import peer.handler.Handler;
import peer.message.MessageDelete;

public class DeletionHandler extends Handler
{
	private static final int MAX_RETRIES = 3;
	private String fileId;

	public DeletionHandler(String fileId)
	{
		this.handlerType = "DeletionHandler";
		this.fileId = fileId;
	}
	
	@Override
	public void run()
	{
		MessageDelete message = new MessageDelete(fileId.getBytes());
		int numberOfRetries = 0;
		int timeToWait = 1000;
		
		//Enhancement: take note of this DELETE message
		Manager.getInstance().registerDelete(fileId);
		
		while(numberOfRetries < MAX_RETRIES)
		{
			send(Channels.MC, message.getMessageBytes());
			
			if (numberOfRetries == 0)
				log("sent deletion message for chunks of file " + Utilities.minifyId(fileId) + ", 1st time");
			else if (numberOfRetries == 1)
				log("sent deletion message for chunks of file " + Utilities.minifyId(fileId) + ", 2nd time");
			else
				log("sent deletion message for chunks of file " + Utilities.minifyId(fileId) + ", 3rd time");
			
			numberOfRetries++;
			try
			{
				Thread.sleep(timeToWait);
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			timeToWait *= 2;
		}
	}
}
