package peer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;

public class ChunkManager implements Serializable
{
	private static final long serialVersionUID = 4951196329264154605L;
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
	
	public synchronized boolean registerChunk(String id, int chunkNo, int repDegree, byte[] data)
	{
		if (findChunk(id, chunkNo) == null)
		{
			Chunk newChunk = new Chunk(id, chunkNo, repDegree, data);
			chunks.add(newChunk);
			return true;
		}
		else return false;
	}
	
	public synchronized boolean storeChunk(String id, int chunkNo)
	{
		Chunk chunk = findChunk(id, chunkNo);
		if (findChunk(id, chunkNo) != null)
		{
			chunk.store();
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
		chunks.remove(chunk);
	}
	
	//returns true if resulting rep count is inferior to desired
	public synchronized boolean increaseReplicationCount(String id, int chunkNo)
	{
		Chunk chunk = findChunk(id, chunkNo);
		if (chunk == null)
			return false;
		return chunk.addToReplicationCount(1);
	}
	
	//same as above
	public synchronized boolean decreaseReplicationCount(String id, int chunkNo)
	{
		Chunk chunk = findChunk(id, chunkNo);
		if (chunk == null)
			return false;
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

	public int getNumberOfChunks()
	{
		return chunks.size();
	}
	
	public Chunk getRandomChunk()
	{
		Random r = new Random();
		if (chunks.size() <= 0)
			return null;
		return chunks.elementAt(r.nextInt(chunks.size()));
	}
	
	public synchronized String getState()
	{
		StringBuilder state = new StringBuilder();
		state.append("CHUNK STORAGE INFORMATION - chunks this peer has stored\n\n")
			 .append("file identificator | chunk number | size (in KB) | current rep. degree | desired rep. degree\n");
		
		for (int i = 0; i < chunks.size(); i++)
		{
			Chunk chunk = chunks.elementAt(i);
			state.append(chunk.getId())
					.append(" | ")
					.append(chunk.getChunkNo())
					.append(" | ")
					.append(chunk.getSize())
					.append(" | ")
					.append(chunk.getRepDegree())
					.append(" | ")
					.append(chunk.getDesiredRepDegree())
					.append("\n");
		}
		
		if (chunks.size() == 0)
			state.append("\t(looks like this peer is not storing any chunk)\n");
		state.append("\n");
		return state.toString();
	}

	public Long getTotalSize()
	{
		long count = 0;
		for (int i = 0; i < chunks.size(); i++)
			count += chunks.elementAt(i).getSize();
		return count;
	}
}
