package peer;

import java.util.concurrent.ConcurrentHashMap;

public class ReclaimManager
{
	ConcurrentHashMap<String, Boolean> interrupted;
	
	public ReclaimManager()
	{
		this.interrupted = new ConcurrentHashMap<String, Boolean>();
	}
	
	public synchronized void registerNewChunk(String fileId, int chunkNo)
	{
		String id = fileId + chunkNo;
		interrupted.put(id, false);
	}
	
	public synchronized void setInterrupted(String fileId, int chunkNo)
	{
		String id = fileId + chunkNo;
		if (interrupted.get(id) != null)
			interrupted.put(id, true);
	}
	
	public synchronized boolean isInterrupted(String fileId, int chunkNo)
	{
		String id = fileId + chunkNo;
		if (interrupted.get(id) != null)
			return interrupted.get(id);
		else return false;
	}
	
	public synchronized void unregisterChunk(String fileId, int chunkNo)
	{
		String id = fileId + chunkNo;
		interrupted.remove(id);
	}
}
