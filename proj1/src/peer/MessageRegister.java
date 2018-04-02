package peer;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class MessageRegister implements Serializable
{
	private static final long serialVersionUID = 3873468185792885428L;
	ConcurrentHashMap<String, Boolean> interrupted;
	
	public MessageRegister()
	{
		this.interrupted = new ConcurrentHashMap<String, Boolean>();
	}
	
	public synchronized void registerNewChunk(String fileId, int chunkNo)
	{
		String id = fileId + chunkNo;
		interrupted.put(id, false); 
	}
	
	public synchronized void setArrived(String fileId, int chunkNo)
	{
		String id = fileId + chunkNo;
		if (interrupted.get(id) != null)
			interrupted.put(id, true);
	}
	
	public synchronized boolean isArrived(String fileId, int chunkNo)
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
