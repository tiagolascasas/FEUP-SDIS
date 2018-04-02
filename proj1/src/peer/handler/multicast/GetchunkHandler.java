package peer.handler.multicast;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import peer.Channels;
import peer.ChunkManager;
import peer.Manager;
import peer.MessageRegister;
import peer.Utilities;
import peer.handler.Handler;
import peer.message.MessageChunk;

public class GetchunkHandler extends Handler
{
	private static final int MAX_WAIT = 400;
	private int chunkNo;
	private String id;
	private int senderId;
	private String version;
	private int destinyPort;
	private String destinyAddr;

	public GetchunkHandler(byte[] message)
	{
		this.handlerType = "GetchunkHandler";
		String[] elements = new String(message, StandardCharsets.US_ASCII).split(" ");
		this.version = elements[1];
		this.senderId = Integer.parseInt(elements[2]);
		this.id = elements[3];
		this.chunkNo = Integer.parseInt(elements[4]);
		
		if (this.version.equals("1.1"))
		{
			this.destinyPort = Integer.parseInt(elements[6]);
			this.destinyAddr = elements[7];
		}
	}

	@Override
	public void run()
	{
		if (this.senderId == Manager.getInstance().getId())
			return;

		ChunkManager manager = Manager.getInstance().getChunkManager();
		byte[] data = manager.retrieveChunkData(id, chunkNo);
		if (data == null)
			return;
		MessageChunk message = new MessageChunk(id.getBytes(), chunkNo, data);
		
		MessageRegister chunkReg = Manager.getInstance().getChunkRegister();
		chunkReg.registerNewChunk(id, chunkNo);
		
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
		if (chunkReg.isArrived(id, chunkNo))
			return;
		chunkReg.unregisterChunk(id, chunkNo);
		
		String peerVersion = Manager.getInstance().getVersion();
		
		boolean enhancedResult = false;
		if (this.version.equals("1.1") && peerVersion.equals("1.1"))
			enhancedResult = enhancedProtocol(message);
		
		//try to use the enhanced version. if the enhanced version fails due to a TCP error,
		//do the default, since it is backwards compatible
		if (enhancedResult)
			return;
		
		if (this.version.equals("1.0") || (this.version.equals("1.1") && peerVersion.equals("1.0")))
		{
			send(Channels.MDR, message.getMessageBytes());
			log("returned chunk no. " + chunkNo + " of file " + Utilities.minifyId(id) + " via Multicast");
		}
		
	}
	
	public boolean enhancedProtocol(MessageChunk message)
	{
		try
		{
			Socket socket = new Socket(this.destinyAddr, this.destinyPort);
			socket.getOutputStream().write(message.getMessageBytes());
			socket.close();
			log("returned chunk no. " + chunkNo + " of file " + Utilities.minifyId(id) + " via TCP");
			return true;
		} 
		catch (IOException e)
		{
			return false;
		}
	}
}
