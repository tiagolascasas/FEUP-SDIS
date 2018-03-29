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
	
	public synchronized Chunk findChunk(String id, int chunkNo)
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
		if (retrieveChunkData(id, chunkNo) == null)
		{
			Chunk newChunk = new Chunk(id, chunkNo, repDegree, data);
			chunks.add(newChunk);
			return true;
		}
		else return false;
	}
	
	public synchronized byte[] retrieveChunkData(String id, int chunkNo)
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
	
	//returns true if resulting rep count is inferior to desired
	public synchronized boolean increaseReplicationCount(String id, int chunkNo)
	{
		Chunk chunk = findChunk(id, chunkNo);
		if (chunk == null)
			return true;
		return chunk.addToReplicationCount(1);
	}
	
	//same as above
	public synchronized boolean decreaseReplicationCount(String id, int chunkNo)
	{
		Chunk chunk = findChunk(id, chunkNo);
		if (chunk == null)
			return true;
		return chunk.addToReplicationCount(-1);
	}

	public synchronized ArrayList<Integer> deleteAllChunksOfFile(String id)
	{
		ArrayList<Chunk> fileChunks = new ArrayList<Chunk>();
		
		for (int i = 0; i < chunks.size(); i++)
		{
			if (chunks.elementAt(i).getId().equals(id))
				fileChunks.add(chunks.elementAt(i));
		}
		chunks.removeAll(fileChunks);

		ArrayList<Integer> chunkNumbers = new ArrayList<Integer>();
		for (int i = 0; i < fileChunks.size(); i++)
		{
			fileChunks.get(i).deleteChunk();
			chunkNumbers.add(fileChunks.get(i).getChunkNo());
		}
		return chunkNumbers;
	}
}
