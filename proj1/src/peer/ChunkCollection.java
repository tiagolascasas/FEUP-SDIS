package peer;

import java.util.Vector;

public class ChunkCollection 
{
	private Vector<Chunk> chunks;
	
	public ChunkCollection()
	{
		chunks = new Vector<Chunk>();
	}
	
	private Chunk findChunk(String id, int chunkNo)
	{
		for (int i = 0; i < chunks.size(); i++)
		{
			if (chunks.elementAt(i).getChunkNo() == chunkNo &&
				chunks.elementAt(i).getId().equals(id))
				return chunks.elementAt(i);
		}
		return null;
	}
	
	public boolean storeChunk(String id, int chunkNo, int repDegree, byte[] data)
	{
		if (retrieveChunk(id, chunkNo) == null)
		{
			Chunk newChunk = new Chunk(id, chunkNo, repDegree, data);
			chunks.add(newChunk);
			return true;
		}
		else return false;
	}
	
	public byte[] retrieveChunk(String id, int chunkNo)
	{
		Chunk chunk = findChunk(id, chunkNo);
		if (chunk != null)
			return chunk.retrieveChunkData();
		else
			return null;
	}
	
	public void deleteChunk(String id, int chunkNo)
	{
		Chunk chunk = findChunk(id, chunkNo);
		chunk.deleteChunk();
	}
	
	public void increaseReplicationCount(String id, int chunkNo, int increment)
	{
		Chunk chunk = findChunk(id, chunkNo);
		chunk.addToReplicationCount(increment);
	}
	
	public boolean decreaseReplicationCount(String id, int chunkNo, int decrement)
	{
		Chunk chunk = findChunk(id, chunkNo);
		return chunk.addToReplicationCount(decrement);
	}

	public byte[] reassembleFile(String id) 
	{
		Vector<Chunk> fileChunks = new Vector<Chunk>();
		for (int i = 0; i < chunks.size(); i++)
		{
			if (chunks.elementAt(i).getId().equals(id))
				fileChunks.add(chunks.elementAt(i));
		}
		fileChunks.sort(null);
		return null;
	}
}
