package peer.handler.rmi;

import java.util.ArrayList;

import peer.Channels;
import peer.Manager;
import peer.Utilities;
import peer.BackupManager;
import peer.handler.Handler;
import peer.message.MessagePutchunk;

public class BackupHandler extends Handler
{
	public static final int MAX_CHUNK_SIZE = 64000;
	private static final int MAX_RETRIES = 5; 
	private byte[] file;
	private byte[] metadata;
	private int repDegree;
	private String fileId;
	
	public BackupHandler(byte[] file, byte[] metadata, int repDegree)
	{
		this.handlerType = "BackupHandler";
		this.file = file;
		this.setMetadata(metadata);
		this.repDegree = repDegree;
		this.fileId = Utilities.calculateFileId(metadata, file).toString();
	}
	
	@Override
	public void run()
	{
		ArrayList<MessagePutchunk> chunks = buildAllChunks();
		BackupManager manager = Manager.getInstance().getBackupsManager();
		manager.createNewEntry(this.fileId, chunks.size());
		int retries = 0;
		int waitingTime = 1000;
		
		while (retries < MAX_RETRIES)
		{
			ArrayList<Integer> chunksToSend = manager.getChunksBelowDegree(this.fileId, this.repDegree);
			log("file id = " + Utilities.minifyId(this.fileId) + ", retries = " + retries + ", chunks below rep deg = " + chunksToSend.size());
	
			if (chunksToSend.size() == 0)
				break;
			for (int i = 0; i < chunksToSend.size(); i++)
			{
				MessagePutchunk chunk = chunks.get(chunksToSend.get(i));
				send(Channels.MDB, chunk.getMessageBytes());
			}
			
			try
			{
				Thread.sleep(waitingTime);
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			retries++;
			waitingTime *= 2;
		}
	}
	
	private ArrayList<MessagePutchunk> buildAllChunks()
	{
		ArrayList<MessagePutchunk> chunks = new ArrayList<MessagePutchunk>();
		int fileSize = file.length;
		int chunkNo = 0;
		int pos = 0;
		boolean lastChunkEmpty = fileSize % MAX_CHUNK_SIZE == 0;
		log("running stored handler for file with id " + Utilities.minifyId(this.fileId) + ", size " + fileSize + " bytes");
		 
		while(fileSize > 0)
		{
			MessagePutchunk message = new MessagePutchunk(fileId.getBytes(), chunkNo, repDegree);
			int chunkSize = (fileSize > MAX_CHUNK_SIZE) ? MAX_CHUNK_SIZE : fileSize;
			byte[] chunkData = new byte[chunkSize];
			System.arraycopy(this.file, pos, chunkData, 0, chunkSize);
			message.setBody(chunkData);
			fileSize -= chunkSize;
			pos += chunkSize;
			chunkNo++;
			chunks.add(message);
		}
		if (lastChunkEmpty)
		{
			MessagePutchunk message = new MessagePutchunk(fileId.getBytes(), chunkNo, repDegree);
			message.setBody(new byte[0]);
			chunks.add(message);
		}
		return chunks;
	}

	public byte[] getMetadata()
	{
		return metadata;
	}

	public void setMetadata(byte[] metadata)
	{
		this.metadata = metadata;
	}
}
