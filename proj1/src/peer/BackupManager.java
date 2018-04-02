package peer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BackupManager implements Serializable
{
	private static final long serialVersionUID = -4404737373903487422L;
	private ConcurrentHashMap<String, ArrayList<Integer>> counts;
	private ConcurrentHashMap<String, Integer> desiredRepDegrees;
	
	public BackupManager()
	{
		this.counts = new ConcurrentHashMap<String, ArrayList<Integer>>();
		this.desiredRepDegrees = new ConcurrentHashMap<String, Integer>();
	}
	
	public synchronized void createNewEntry(String fileId, int numberOfChunks, int desiredRepDegree)
	{
		ArrayList<Integer> arr = new ArrayList<Integer>(Collections.nCopies(numberOfChunks, 0));
		counts.put(fileId, arr);
		desiredRepDegrees.put(fileId, desiredRepDegree);
	}
	
	public boolean hasInitiated(String fileId)
	{
		return counts.get(fileId) == null ? false : true; 
	}
	
	public synchronized boolean increment(String fileId, int chunkNo)
	{
		if (counts.get(fileId) == null)
			return false;
		counts.get(fileId).set(chunkNo, counts.get(fileId).get(chunkNo) + 1);
		return true;
	}
	
	public synchronized boolean decrement(String fileId, int chunkNo)
	{
		if (counts.get(fileId) == null)
			return false;
		counts.get(fileId).set(chunkNo, counts.get(fileId).get(chunkNo) - 1);
		return true;
	}
	
	public int getReplicationDegree(String fileId, int chunkNo)
	{
		return counts.get(fileId).get(chunkNo);
	}
	
	public synchronized ArrayList<Integer> getChunksBelowDegree(String fileId, int degree)
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
	
	public synchronized String getState()
	{
		StringBuilder state = new StringBuilder();
		state.append("INITIATOR PEER INFORMATION - chunks this file was ordered to backup by the client\n\n")
		     .append("file identificator | desired rep degree | [actual rep degree of each chunk (first pos = chunk no. 0)]\n\n");
		
		for (Map.Entry<String, ArrayList<Integer>> entry : counts.entrySet())
		{
			String fileId = entry.getKey();
			int repDeg = desiredRepDegrees.get(fileId);
			ArrayList<Integer> fileChunks = entry.getValue();
			
			state.append(fileId);
			state.append(" | ");
			state.append(repDeg);
			state.append(" | ");
			state.append(fileChunks);
			state.append("\n");
		}
		
		if (counts.size() == 0)
			state.append("\t(looks like this peer never initiated any backup)\n");
		state.append("\n");
		
		return state.toString();
	}
}
