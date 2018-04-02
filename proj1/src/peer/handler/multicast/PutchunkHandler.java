package peer.handler.multicast;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

import peer.BackupManager;
import peer.Channels;
import peer.ChunkManager;
import peer.Manager;
import peer.MessageRegister;
import peer.Utilities;
import peer.handler.Handler;
import peer.message.Message;
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
		
		if (this.senderId == Manager.getInstance().getId())
			return;

		ArrayList<Byte> allData = new ArrayList<Byte>();
		int dataStart = -1;
		for (int i = 0; i < message.length; i++)
		{
			if (message[i] == Message.CR &&
				message[i+1] == Message.LF &&
				message[i+2] == Message.CR &&
				message[i+3] == Message.LF)
			{
				dataStart = i + 4;
				break;
			}
		}
		if (dataStart >= message.length)
			return;
		
		for (int i = dataStart; i < message.length; i++)
			allData.add(message[i]);
		this.data = new byte[allData.size()];
		for (int i = 0; i < allData.size(); i++)
			this.data[i] = allData.get(i);
	}
	
	@Override
	public void run()
	{	
		//Enhancement: if peer receives a Putchunk for a file it sent a delete order
		//previously, then it should remove the record of that order
		//this should be done even if this was the initiator peer for the backup
		Manager.getInstance().unregisterDelete(this.id);
		
		if (this.senderId == Manager.getInstance().getId())
			return;
		
		if (!Manager.getInstance().getAllowSaving())
			return;
		
		//a peer that was ordered to store a file can never store a chunk of that file
		BackupManager man = Manager.getInstance().getBackupsManager();
		if (man.hasInitiated(id))
			return;
		
		MessageRegister putchunkReg = Manager.getInstance().getPutchunkRegister();
		putchunkReg.setArrived(id, chunkNo);
		
		ChunkManager manager = Manager.getInstance().getChunkManager();
		
		if (!manager.registerChunk(id, chunkNo, 0, data))
			return;
		
		//Default behaviour: store as soon as it receives the message
		if (Manager.getInstance().getVersion().equals("1.0"))
		{
			manager.storeChunk(id, chunkNo);
			log("stored chunk no. " + chunkNo + " of file " + Utilities.minifyId(id));
		}
		
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
		
		//Default behaviour: send STORED message after waiting
		if (Manager.getInstance().getVersion().equals("1.0"))
		{
			send(Channels.MC, reply.getMessageBytes());
			return;
		}
		
		//Enhanced behaviour: check if rep. degree is below expected, and if such, 
		//store the chunk and send the STORED message. 
		//Otherwise, unregister the chunk and do nothing
		if (Manager.getInstance().getVersion().equals("1.1"))
		{
			int currentRepDeg = manager.findChunk(id, chunkNo).getRepDegree();
			if (currentRepDeg >= this.repDeg)
			{
				manager.deleteChunk(id, chunkNo);
				log("ignored chunk no. " + chunkNo + " of file " + Utilities.minifyId(id) + ", rep deg is superior than " + this.repDeg);
			}
			else
			{
				manager.storeChunk(id, chunkNo);
				manager.increaseReplicationCount(id, chunkNo);
				send(Channels.MC, reply.getMessageBytes());
				log("stored chunk no. " + chunkNo + " of file " + Utilities.minifyId(id));
			}
		}
	}
}
