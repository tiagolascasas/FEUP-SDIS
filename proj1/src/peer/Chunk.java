package peer;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

public class Chunk implements Comparator
{
	private String id;
	private int chunkNo;
	private int repDegree;
	private String filePath;
	
	Chunk(String id, int chunkNo, int repDegree, byte[] data)
	{
		this.setId(id);
		this.setChunkNo(chunkNo);
		this.setRepDegree(repDegree);
		String fileName = id + "#" + chunkNo;
		this.filePath = DataManager.getInstance().getPath(fileName); 
		try 
		{
			File file = new File(filePath);
			DataOutputStream stream = new DataOutputStream(new FileOutputStream(file));
			stream.write(data, 0, data.length);
			stream.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getId() 
	{
		return id;
	}

	private void setId(String id) 
	{
		this.id = id;
	}

	public int getChunkNo() 
	{
		return chunkNo;
	}

	private void setChunkNo(int chunkNo) 
	{
		this.chunkNo = chunkNo;
	}

	public int getRepDegree() 
	{
		return repDegree;
	}

	public void setRepDegree(int repDegree) 
	{
		this.repDegree = repDegree;
	}
	
	public byte[] retrieveChunkData()
	{
		Path path = Paths.get(this.filePath);
		byte[] fileContents = null;
		try 
		{
			fileContents =  Files.readAllBytes(path);
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileContents;
	}
	
	public void deleteChunk()
	{
		File file = new File(this.filePath);
		file.delete();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		return this.id.equals(((Chunk) obj).getId()) && 
				this.chunkNo == ((Chunk) obj).getChunkNo();
	}

	public boolean addToReplicationCount(int value) 
	{
		this.repDegree += value;
		return this.repDegree > 0;
	}

	@Override
	public int compare(Object o1, Object o2) 
	{
		if (((Chunk)o1).getChunkNo() < ((Chunk)o2).getChunkNo())
			return -1;
		else if (((Chunk)o1).getChunkNo() > ((Chunk)o2).getChunkNo())
			return 1;
		else
			return 0;
	}
}
