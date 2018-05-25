package com.client.handlers;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import com.client.ClientManager;


public class ResponseHandlerDownload extends ResponseHandler
{
	private String message;
	private static final String STORAGE_DIR = "downloads";

	public ResponseHandlerDownload(String status, String message)
	{
		super("RES_DOWNLOAD", status);
		this.message = message;
	}

	@Override
	public void run()
	{
		if (status == 0)
			ClientManager.getInstance().setLoggedIn(true);

		String decodedMessage = new String(Base64.getDecoder().decode(this.message));
		
		String[] split = decodedMessage.split(" ");
		
		for(int i = 0; i<split.length; i++) {
			System.out.println(split[i]);
		};
		
		String title = new String(Base64.getDecoder().decode(new String(Base64.getDecoder().decode(split[0]),StandardCharsets.US_ASCII)));

		byte[] data = Base64.getDecoder().decode(split[1]);
		
		int onSave = this.save(title, data);
		
		String res = ""; 
		
		if(onSave == 1)
			res = "Downloaded file " + title + ".\0";
		else
			res = "Couldn't save file " + title + "on system.\0";
		
		ClientManager.getInstance().log(res);
	}
	
	public int save(String title, byte[] data)
	{	
		String path = STORAGE_DIR + "/" + title;
		try
		{		
			File file = new File(path);
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
			System.out.println("Error while writting to " + path);
			return 0;
		}
	}

}
