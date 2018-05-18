package server;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class FileStorage implements Serializable
{
	private static final long serialVersionUID = 4832308080638012451L;
	private static final String STORAGE_DIR = "storage";
	private ArrayList<String> storedFiles;

	public FileStorage()
	{
		this.storedFiles = new ArrayList<>();
	}
	
	public boolean save(String title, byte[] data)
	{
		title = Utils.decode(title);
		data = Utils.decode(new String(data)).getBytes();
		
		String path = STORAGE_DIR + "/" + title;
		try
		{	/*
			//TODO: verificar se isto conta como I/O ass�ncrono
			AsynchronousFileChannel fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.WRITE);
			ByteBuffer buffer = ByteBuffer.allocate(data.length);
			buffer.put(data);
			fileChannel.write(buffer, 0);*/
			
			File file = new File(path);
			if (data != null) 
			{
				DataOutputStream stream = new DataOutputStream(new FileOutputStream(file));
				stream.write(data, 0, data.length);
				stream.close();
			}
			else
				return false;
			
			this.storedFiles.add(title);
			return true;
		} 
		catch (IOException e)
		{
			System.out.println("Error while writting to " + path);
			return false;
		}
	}

	//TODO: melhorar a pesquisa
	public ArrayList<String> search(String title)
	{
		String regex = Pattern.quote(title);
		ArrayList<String> list = new ArrayList<>();
		
		for (String file : this.storedFiles)
		{
			if (file.matches(regex))
				list.add(file);
		}
		return list;
	}

	public byte[] retrieve(String title)
	{
		title = Utils.decode(title);
		String path = STORAGE_DIR + "/" + title;

		if (storedFiles.indexOf(title) == -1)
			return null;

		byte[] data = null;
		
		try {
			File file = new File(path);	
			if(!file.exists())
				System.out.println("File doesn't Exist on storage!");
			else if(!file.isFile())
				System.out.println("File is not File on storage!");
			data = Files.readAllBytes(file.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		//TODO: usar I/O ass�ncrono para ler o ficheiro
		return data;
	}

}
