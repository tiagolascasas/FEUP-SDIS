package peer.handler.multicast;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.TreeSet;

import peer.Manager;
import peer.handler.Handler;
import peer.handler.rmi.DeletionHandler;

public class OnlineHandler extends Handler
{
	private int senderId;

	public OnlineHandler(byte[] message)
	{
		this.handlerType = "OnlineHandler";
		String[] elements = new String(message, StandardCharsets.US_ASCII).split(" ");
		this.senderId = Integer.parseInt(elements[2]);
	}
	
	@Override
	public void run()
	{
		if (this.senderId == Manager.getInstance().getId())
			return;
		log("received notification that peer " + senderId + " is now online");
		
		TreeSet<String> deletedSent = Manager.getInstance().getDeletedSent();
		if (deletedSent.size() > 0)
		{
			log("sending DELETE messages for " + deletedSent.size() + " files");
			for (String file : deletedSent)
			{
				DeletionHandler handler = new DeletionHandler(file);
				handler.start();
			}
		}
	}
}
