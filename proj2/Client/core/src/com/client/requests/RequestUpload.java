package com.client.requests;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import com.client.ClientManager;

public class RequestUpload extends Request
{
	private String track;
	private static final String[] FORMATS = {".mp3", ".ogg", ".wav"};
	private static final String LOCAL_LIB = "local_library";

	public RequestUpload(String track)
	{
		super("UPLOAD");
		this.track = track;
	}
	
	@Override
	public void run()
	{
		if (!checkAuthenticated())
			return;
		
		boolean validFormat = false;
		for (String format : FORMATS)
		{
			if (this.track.toLowerCase().endsWith(format))
			{
				validFormat = true;
				break;
			}
		}
		if (!validFormat)
		{
			ClientManager.getInstance().log("Error: specified file is not an audio file. Accepted formats are .mp3, .ogg and .wav.");
			return;
		}
		
		Path path;
		
		if(!(new File(this.track)).exists()) 
			path = FileSystems.getDefault().getPath(LOCAL_LIB + "/", this.track);
		else 
			path = FileSystems.getDefault().getPath(this.track);	
		
		
		String paths[] = this.track.split("/");
		String filename = paths[paths.length-1];

		byte[] fileData = null;
        try
		{
			fileData = Files.readAllBytes(path);
			
			if(!path.startsWith(LOCAL_LIB + "/"))
				save_to_local(filename, fileData);
		}
        catch (IOException e)
		{
			ClientManager.getInstance().log("Error accessing file " + this.track);
			return;
		}
        
        String encodedName = new String(Base64.getEncoder().encode(filename.getBytes()));
        String encodedFile = new String(Base64.getEncoder().encode(fileData));
        
        String message = getMessageHeader() + " " + encodedName + " " + encodedFile + "\0";
        send(message);
	}

	private int save_to_local(String title, byte[] data) {
		
		try
		{		
			File file = new File(LOCAL_LIB + "/" + title);
			if (file.exists())
				return -1;
			
			if (data != null) 
			{
				DataOutputStream stream = new DataOutputStream(new FileOutputStream(file));
				stream.write(data, 0, data.length);
				stream.close();
			}
			else
				return 0;
			
			return 1;
		} 
		catch (IOException e)
		{
			System.out.println("Error while writting to " + LOCAL_LIB + "/" + title);
			return 0;
		}
		
	}
	
}
