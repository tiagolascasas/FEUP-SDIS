package peer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import peer.handler.rmi.BackupHandler;

public class RestoreManager implements Serializable
{
	private static final long serialVersionUID = -2952517571955987887L;
	private static ConcurrentHashMap<String, ArrayList<ArrayList<Byte>>> files;
	
	public RestoreManager()
	{
		files = new ConcurrentHashMap<String, ArrayList<ArrayList<Byte>>>();
	}
	
	public synchronized void createNewEntry(String fileId, int numberOfChunks)
	{
		ArrayList<ArrayList<Byte>> file = new ArrayList<ArrayList<Byte>>();
		for (int i = 0; i < numberOfChunks; i++)
			file.add(null);
		files.put(fileId, file);
	}
	
	public synchronized boolean storeChunk(String fileId, int chunkNo, byte[] data)
	{
		ArrayList<ArrayList<Byte>> file = files.get(fileId);
		if (file == null)
			return false;
		
		if (file.get(chunkNo) != null)
			return false;
		
		if (data == null && chunkNo == file.size() - 1)
		{
			file.remove(chunkNo);
			return true;
		}
		
		ArrayList<Byte> dataArray = new ArrayList<Byte>();
		for (int i = 0; i < data.length; i++)
			dataArray.add(data[i]);
		
		file.set(chunkNo, dataArray);
		files.put(fileId, file);
		return true;
	}
	
	public synchronized boolean isComplete(String fileId)
	{
		ArrayList<ArrayList<Byte>> file = files.get(fileId);
		if (file == null)
			return false;
		
		for (int i = 0; i < file.size(); i++)
		{
			ArrayList<Byte> chunk = file.get(i);
			if (chunk == null)
				return false;
		}
		return true;
	}
	
	public synchronized byte[] reassemble(String fileId)
	{
		if (!isComplete(fileId))
			return null;
		
		ArrayList<ArrayList<Byte>> file = files.get(fileId);
		byte[] reassembly = new byte[file.size() * BackupHandler.MAX_CHUNK_SIZE * 2];
		
		int index = 0;
		for (int i = 0; i < file.size(); i++)
		{
			ArrayList<Byte> chunk = file.get(i);
			int size = chunk.size();
			for (int j = 0; j < size; j++)
			{
				reassembly[index] = chunk.get(j);
				index++;
			}
		}
		return Arrays.copyOfRange(reassembly, 0, index);
	}
}
