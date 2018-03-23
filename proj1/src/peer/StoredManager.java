package peer;

import java.lang.reflect.Array;
import java.util.concurrent.ConcurrentHashMap;

public class StoredManager 
{
	private ConcurrentHashMap<String, Array> counts; 
	
	public StoredManager()
	{
		this.counts = new ConcurrentHashMap<String, Array>();
	}
	
	public void increment(String fileId, int chunkNo)
	{
		
	}
	
	public int getReplicationDegree(String fileId, int chunkNo)
	{
		return 0;
	}
	
	public int[] getChunksBelowDegree(int degree)
	{
		return null;
	}
}
