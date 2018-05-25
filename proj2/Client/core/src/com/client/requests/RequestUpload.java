package com.client.requests;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import com.client.ClientManager;

public class RequestUpload extends Request
{
	private String track;

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
		
		Path path = FileSystems.getDefault().getPath("", this.track);
		
		String paths[] = this.track.split("/");
		String filename = paths[paths.length-1];

		byte[] fileData = null;
        try
		{
			fileData = Files.readAllBytes(path);
			
		}
        catch (IOException e)
		{
			ClientManager.getInstance().log("Error accessing file " + this.track);
			return;
		}
        
        System.out.println(fileData);
        
        String encodedName = new String(Base64.getEncoder().encode(filename.getBytes()));
        String encodedFile = new String(Base64.getEncoder().encode(fileData));
        
        String message = getMessageHeader() + " " + encodedName + " " + encodedFile + "\0";
        System.out.println(message);
        send(message);
	}
}
