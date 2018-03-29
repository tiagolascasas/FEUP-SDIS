package peer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

public class BackupManager 
{
	private ConcurrentHashMap<String, ArrayList<Integer>> counts; 
	
	public BackupManager()
	{
		this.counts = new ConcurrentHashMap<String, ArrayList<Integer>>();
	}
	
	public void createNewEntry(String fileId, int numberOfChunks)
	{
		ArrayList<Integer> arr = new ArrayList<Integer>(Collections.nCopies(numberOfChunks, 0));
		counts.put(fileId, arr);
	}
	
	public boolean increment(String fileId, int chunkNo)
	{
		if (counts.get(fileId) == null)
			return false;
		counts.get(fileId).set(chunkNo, counts.get(fileId).get(chunkNo) + 1);
		return true;
	}
	
	public int getReplicationDegree(String fileId, int chunkNo)
	{
		return counts.get(fileId).get(chunkNo);
	}
	
	public ArrayList<Integer> getChunksBelowDegree(String fileId, int degree)
	{
		ArrayList<Integer> repDegs = counts.get(fileId);
		ArrayList<Integer> res = new ArrayList<Integer>();
		
		for (int i = 0; i < repDegs.size(); i++)
		{
			int repDeg = repDegs.get(i);
			if (repDeg < degree)
				res.add(i);
		}
		return res;
	}
}
