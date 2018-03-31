package peer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
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
	
	public synchronized String getState()
	{/*
		StringBuilder state = new StringBuilder();
		state.append("RECLAIM/REMOVED MANAGER - information about the the chunks this peer started the\n")
			 .append("backup subprotocol upon getting an insufficient rep. degree for that chunk.\n")
			 .append("It registers the PUTCHUNK chunks it receives so that it doesn't start that subprotocol\n")
			 .append("if it receives that message within the random 0-400ms it must wait\n\n")
			 .append("file identificator | chunk number | received? (yes/no)\n\n");
		for (Entry<String, Boolean> entry : interrupted.entrySet())
		{
			String fileId = entry.getKey();
			String[] fields = fileId.split("#");
			boolean received = entry.getValue();
			state.append(fields[0])
			     .append(" | ")
			     .append(fields[1])
			     .append("|")
			     .append(received ? "yes\n" : "no\n");
		}
		if (interrupted.size() == 0)
			state.append("\t(looks like this peer is not initializing any backup subprotocol after a removed message receival)\n");
		state.append("\n");
		return state.toString();*/
		return "\n";
	}
}
