package peer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Manager
{
	private static Manager instance = new Manager();

	//store the peer's basic information and sockets for easy access
	private String version;
	private int id;
	private McastID mc, mdr, mdb;
	private MulticastSocket mcSocket, mdrSocket, mdbSocket;
	
	//single thread-safe manager to manage stored chunks
	private static ChunkManager chunks = new ChunkManager();
	//single thread-safe manager to manage files this peer told other peers to backup
	private static BackupManager backups = new BackupManager();
	//single thread-safe manager to manage files this peer was told to restore
	private static RestoreManager restores = new RestoreManager();
	
	private Manager() {}
	
	public static Manager getInstance()
	{
		return instance;
	}
	
	public void init(String version, int id, McastID[] connections)
	{
		this.setVersion(version);
		this.setId(id);
		this.mc = connections[0];
		this.mdb = connections[1];
		this.mdr = connections[2];
	}
	
	public void setSockets(MulticastSocket mc, MulticastSocket mdr, MulticastSocket mdb)
	{
		this.mcSocket = mc;
		this.mdrSocket = mdr;
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
	
	public InetAddress getAddress(Channels channel)
	{
		if (channel == Channels.MC)
			return mc.addr;
		if (channel == Channels.MDB)
			return mdb.addr;
		if (channel == Channels.MDR)
			return mdr.addr;
		return null;
	}
	
	public int getPort(Channels channel)
	{
		if (channel == Channels.MC)
			return mc.port;
		if (channel == Channels.MDB)
			return mdb.port;
		if (channel == Channels.MDR)
			return mdr.port;
		return -1;
	}
	
	public MulticastSocket getSocket(Channels channel)
	{
		if (channel == Channels.MC)
			return mcSocket;
		if (channel == Channels.MDB)
			return mdbSocket;
		if (channel == Channels.MDR)
			return mdrSocket;
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
			e.printStackTrace();
		}
		return "backups_peer" + id + "/" + fileName;
	}
	
	public ChunkManager getChunkManager()
	{
		return chunks;
	}
	
	public BackupManager getStoredManager()
	{
		return backups;
	}
	
	public RestoreManager getRestoredManager()
	{
		return restores;
	}
}
