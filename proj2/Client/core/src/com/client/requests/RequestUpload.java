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
        
        byte[] encodedName = Base64.getEncoder().encode(this.track.getBytes());
        byte[] encodedFile = Base64.getEncoder().encode(fileData);
        
        String message = getMessageHeader() + " " + encodedName + " " + encodedFile + "\0";
        send(message);
	}
}
