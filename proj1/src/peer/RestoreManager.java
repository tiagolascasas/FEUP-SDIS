package peer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import peer.handler.rmi.BackupHandler;

public class RestoreManager
{
	private static ConcurrentHashMap<String, ArrayList<ArrayList<Byte>>> files;
	
	public RestoreManager()
	{
		files = new ConcurrentHashMap<String, ArrayList<ArrayList<Byte>>>();
	}
	
	public synchronized void createNewEntry(String fileId, int numberOfChunks)
	{
		files.put(fileId, new ArrayList<ArrayList<Byte>>(numberOfChunks));
	}
	
	public synchronized void storeChunk(String fileId, int chunkNo, byte[] data)
	{
		ArrayList<Byte> dataArray = new ArrayList<Byte>(data.length);
		for (int i = 0; i < data.length; i++)
			dataArray.set(i, data[i]);
		ArrayList<ArrayList<Byte>> file = files.get(fileId);
		file.set(chunkNo, dataArray);
		files.put(fileId, file);
	}
	
	public synchronized boolean isComplete(String fileId)
	{
		ArrayList<ArrayList<Byte>> file = files.get(fileId);
		return !file.contains(null);
	}
	
	public synchronized byte[] reassemble(String fileId)
	{
		if (!isComplete(fileId))
			return null;
		
		ArrayList<ArrayList<Byte>> file = files.get(fileId);
		byte[] reassembly = new byte[files.size() * BackupHandler.MAX_CHUNK_SIZE];
		
		int index = 0;
		for (int i = 0; i < file.size(); i++, index++)
		{
			ArrayList<Byte> chunk = file.get(i);
			for (int j = 0; j < chunk.size(); j++, index++)
				reassembly[index] = chunk.get(j);
		}
		return Arrays.copyOfRange(reassembly, 0, index);
	}
}
