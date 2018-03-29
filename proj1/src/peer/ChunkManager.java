package peer;

import java.util.ArrayList;
import java.util.Vector;

public class ChunkManager 
{
	private Vector<Chunk> chunks;
	
	public ChunkManager()
	{
		chunks = new Vector<Chunk>();
	}
	
	private synchronized Chunk findChunk(String id, int chunkNo)
	{
		for (int i = 0; i < chunks.size(); i++)
		{
			if (chunks.elementAt(i).getChunkNo() == chunkNo &&
				chunks.elementAt(i).getId().equals(id))
				return chunks.elementAt(i);
		}
		return null;
	}
	
	public synchronized boolean storeChunk(String id, int chunkNo, int repDegree, byte[] data)
	{
		if (retrieveChunk(id, chunkNo) == null)
		{
			Chunk newChunk = new Chunk(id, chunkNo, repDegree, data);
			chunks.add(newChunk);
			return true;
		}
		else return false;
	}
	
	public synchronized byte[] retrieveChunk(String id, int chunkNo)
	{
		Chunk chunk = findChunk(id, chunkNo);
		if (chunk != null)
			return chunk.retrieveChunkData();
		else
			return null;
	}
	
	public synchronized void deleteChunk(String id, int chunkNo)
	{
		Chunk chunk = findChunk(id, chunkNo);
		chunk.deleteChunk();
	}
	
	public synchronized void increaseReplicationCount(String id, int chunkNo, int increment)
	{
		Chunk chunk = findChunk(id, chunkNo);
		chunk.addToReplicationCount(increment);
	}
	
	public synchronized boolean decreaseReplicationCount(String id, int chunkNo, int decrement)
	{
		Chunk chunk = findChunk(id, chunkNo);
		return chunk.addToReplicationCount(decrement);
	}

	public synchronized int deleteAllChunksOfFile(String id)
	{
		ArrayList<Chunk> fileChunks = new ArrayList<Chunk>();
		
		for (int i = 0; i < chunks.size(); i++)
		{
			if (chunks.elementAt(i).getId().equals(id))
				fileChunks.add(chunks.elementAt(i));
		}
		chunks.removeAll(fileChunks);

		for (int i = 0; i < fileChunks.size(); i++)
			fileChunks.get(i).deleteChunk();
		return fileChunks.size();
	}
}
