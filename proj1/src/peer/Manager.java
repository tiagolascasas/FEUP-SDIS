package peer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	private boolean allowSaving;
	
	//single thread-safe manager to manage stored chunks
	private static ChunkManager chunks;
	//single thread-safe manager to manage files this peer told other peers to backup
	private static BackupManager backups;
	//single thread-safe manager to manage files this peer was told to restore
	private static RestoreManager restores;
	//single thread-safe manager to manage chunks that arrived during a transmission in the reclaim protocol
	private static ReclaimManager reclaims;
	
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
		this.allowSaving = true;
		restoreState();
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
	
	public BackupManager getBackupsManager()
	{
		return backups;
	}
	
	public RestoreManager getRestoredManager()
	{
		return restores;
	}
	
	public ReclaimManager getReclaimManager()
	{
		return reclaims;
	}

	public void setAllowSaving(boolean allow)
	{
		this.allowSaving = allow;
	}

	public boolean getAllowSaving()
	{
		return allowSaving;
	}
	
	public void saveState()
	{
		String serName = "state_peer" + this.id;
		try
		{
			ObjectOutputStream outStr = new ObjectOutputStream(new FileOutputStream(new File(serName)));
			outStr.writeObject(Manager.chunks);
			outStr.writeObject(Manager.backups);
			outStr.writeObject(Manager.restores);
			outStr.writeObject(Manager.reclaims);
			outStr.close();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	private void restoreState()
	{
		String serName = "state_peer" + this.id;
		try
		{
			InputStream inputStream = new BufferedInputStream(new FileInputStream(serName));
			ObjectInput objectStream = new ObjectInputStream(inputStream);
			Manager.chunks = (ChunkManager)objectStream.readObject();
			Manager.backups = (BackupManager)objectStream.readObject();
			Manager.restores = (RestoreManager)objectStream.readObject();
			Manager.reclaims = (ReclaimManager)objectStream.readObject();
			objectStream.close();
		} 
		catch (IOException | ClassNotFoundException e)
		{
			System.out.println("No serialized state found; starting from a blank state"); 
			Manager.backups = new BackupManager();
			Manager.chunks = new ChunkManager();
			Manager.restores = new RestoreManager();
			Manager.reclaims = new ReclaimManager();
			return;
		}
	}
	
	public synchronized String getCurrentState()
	{
		int currentOccupiedSpace = chunks.getNumberOfChunks() * 64000;
		File disk = new File(".");
		long freeSpace = disk.getFreeSpace();
		
		StringBuilder state = new StringBuilder();
		state.append("STATE OF PEER ")
		      .append(Manager.getInstance().getId())
		      .append("\n\n")
		      .append("-------------------------------------------------------------------------------------\n")
		      .append("Space used by chunks: ")
		      .append(currentOccupiedSpace)
		      .append(" bytes\nFree space: ")
		      .append(freeSpace / 1000000000)
		      .append(" Gb (")
		      .append(freeSpace)
		      .append(" bytes)\n\n)")
		      .append("-------------------------------------------------------------------------------------\n")
		      .append(chunks.getState())
		      .append("-------------------------------------------------------------------------------------\n")
		      .append(backups.getState())
		      .append("-------------------------------------------------------------------------------------\n")
		      .append(restores.getState())
		      .append("-------------------------------------------------------------------------------------\n")
		      .append(reclaims.getState())
		      .append("\n");
		return state.toString();
	}
}
