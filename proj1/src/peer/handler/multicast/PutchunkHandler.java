package peer.handler.multicast;

import java.nio.charset.StandardCharsets;
import java.util.Random;

import peer.CHANNELS;
import peer.DataManager;
import peer.handler.Handler;
import peer.message.MessageStored;

public class PutchunkHandler extends Handler
{
	private static final int MAX_WAIT = 400;
	private byte[] data;
	private int senderId;
	private String id;
	private int repDeg;
	private int chunkNo;
	
	public PutchunkHandler(byte[] message)
	{
		this.handlerType = "PutchunkHandler";
		String[] elements = new String(message, StandardCharsets.US_ASCII).split(" ");
		this.senderId = Integer.parseInt(elements[2]);
		this.id = elements[3];
		this.chunkNo = Integer.parseInt(elements[4]);
		this.repDeg = Integer.parseInt(elements[5]);
		
		StringBuilder fullData = new StringBuilder();
		for (int i = 6; i < elements.length; i++)
			fullData.append(elements[i]);
		this.data = fullData.toString().getBytes();
	}
	
	@Override
	public void run()
	{	
		if (this.senderId == DataManager.getInstance().getId())
			return;
		
		if (!DataManager.getInstance().store(id, chunkNo, repDeg, data))
			return;
		log("stored chunk no " + chunkNo + " of file with id " + id);
		
		MessageStored reply = new MessageStored(id.getBytes(), chunkNo);
		Random r = new Random();
		int time = r.nextInt(MAX_WAIT);
		try
		{
			Thread.sleep(time);
		} 
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		send(CHANNELS.MC, reply.getMessageBytes());
	}
}
