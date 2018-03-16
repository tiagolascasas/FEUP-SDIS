package peer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataManager
{
	private static DataManager instance = new DataManager();
	private String version;
	private int id;
	private ChunkCollection chunks = new ChunkCollection();
	
	private DataManager() {}
	
	public static DataManager getInstance()
	{
		return instance;
	}
	
	public void init(String version, int id)
	{
		this.setVersion(version);
		this.setId(id);
	}

	public String getVersion()
	{
		return version;
	}

	private void setVersion(String version)
	{
		this.version = version;
	}

	public int getId()
	{
		return id;
	}

	private void setId(int id)
	{
		this.id = id;
	}

	public String getPath(String fileName) 
	{
		try 
		{
			Files.createDirectories(Paths.get("backups_peer" + id));
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "backups_peer" + id + "/" + fileName;
	}
	
	public synchronized void store(String id, int chunkNo, int repDeg, byte[] data)
	{
		this.chunks.storeChunk(id, chunkNo, repDeg, data);
	}
	
	public synchronized byte[] retrieve(String id, int chunkNo)
	{
		return this.chunks.retrieveChunk(id, chunkNo);
	}
	
	public synchronized void delete(String id, int chunkNo)
	{
		this.chunks.deleteChunk(id, chunkNo);
	}
	
	public synchronized void increaseCount(String id, int chunkNo, int increment)
	{
		this.chunks.increaseReplicationCount(id, chunkNo, increment);
	}
	
	public synchronized boolean decreaseCount(String id, int chunkNo, int decrement)
	{
		return this.chunks.decreaseReplicationCount(id, chunkNo, decrement);
	}
	
	public synchronized byte[] reassemble(String id)
	{
		return this.chunks.reassembleFile(id);
	}
}
