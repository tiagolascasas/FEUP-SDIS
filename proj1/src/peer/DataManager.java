package peer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataManager
{
	private static DataManager instance = new DataManager();
	private String version;
	private int id;
	private ChunkCollection chunks = new ChunkCollection();
	private McastID mc, mbr, mdb;
	private MulticastSocket mcSocket, mbrSocket, mdbSocket;
	
	private DataManager() {}
	
	public static DataManager getInstance()
	{
		return instance;
	}
	
	public void init(String version, int id, McastID[] connections)
	{
		this.setVersion(version);
		this.setId(id);
		this.mc = connections[0];
		this.mdb = connections[1];
		this.mbr = connections[2];
	}
	
	public void setSockets(MulticastSocket mc, MulticastSocket mbr, MulticastSocket mdb)
	{
		this.mcSocket = mc;
		this.mbrSocket = mbr;
		this.mdbSocket = mdb;
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
	
	public InetAddress getAddress(CHANNELS channel)
	{
		if (channel == CHANNELS.MC)
			return mc.addr;
		if (channel == CHANNELS.MDB)
			return mdb.addr;
		if (channel == CHANNELS.MBR)
			return mbr.addr;
		return null;
	}
	
	public int getPort(CHANNELS channel)
	{
		if (channel == CHANNELS.MC)
			return mc.port;
		if (channel == CHANNELS.MDB)
			return mdb.port;
		if (channel == CHANNELS.MBR)
			return mbr.port;
		return -1;
	}
	
	public MulticastSocket getSocket(CHANNELS channel)
	{
		if (channel == CHANNELS.MC)
			return mcSocket;
		if (channel == CHANNELS.MDB)
			return mdbSocket;
		if (channel == CHANNELS.MBR)
			return mbrSocket;
		return null;
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
