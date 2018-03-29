package peer.handler.rmi;

import java.io.File;

import peer.Channels;
import peer.Chunk;
import peer.ChunkManager;
import peer.Manager;
import peer.Utilities;
import peer.handler.Handler;
import peer.message.MessageRemoved;

public class ReclamationHandler extends Handler
{
	private int desiredFinalSpace;
	
	public ReclamationHandler(int desiredFinalSpace)
	{
		this.handlerType = "ReclamationHandler";
		this.desiredFinalSpace = desiredFinalSpace;
	}
	
	@Override
	public void run()
	{
		ChunkManager manager = Manager.getInstance().getChunkManager();
		
		int currentOccupiedSpace = manager.getNumberOfChunks() * 64000;
		if (currentOccupiedSpace < desiredFinalSpace)
			return;
		
		File disk = new File(".");
		long maxDiskSpace = disk.getTotalSpace();
		if (maxDiskSpace < desiredFinalSpace)
			return;
		
		int chunksToDelete = (currentOccupiedSpace - desiredFinalSpace) / 64000 + 1;
		Manager.getInstance().setAllowSaving(false);
		log("deleting at most " + chunksToDelete + " chunk(s) to get a total size of " + desiredFinalSpace + " (curr space = " + currentOccupiedSpace + " bytes)");
		while (chunksToDelete > 0)
		{
			Chunk chunk = manager.getRandomChunk();
			if (chunk == null)
				break;
			manager.deleteChunk(chunk.getId(), chunk.getChunkNo());
			log("deleted chunk no " + chunk.getChunkNo() + " of file with id = " + Utilities.minifyId(chunk.getId()));
			MessageRemoved message = new MessageRemoved(chunk.getId().getBytes(), chunk.getChunkNo());
			send(Channels.MC, message.getMessageBytes());
			chunksToDelete--;
		}
		Manager.getInstance().setAllowSaving(true);
	}
}
