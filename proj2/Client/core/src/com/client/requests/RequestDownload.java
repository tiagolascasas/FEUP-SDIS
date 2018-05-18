package com.client.requests;

import java.util.Base64;

public class RequestDownload extends Request
{
	private String track;

	public RequestDownload(String track)
	{
		super("DOWNLOAD");
		this.track = track;
	}
	
	@Override
	public void run()
	{
		if (!checkAuthenticated())
			return;
        
        String encodedName = new String(Base64.getEncoder().encode(this.track.getBytes()));
        
        String message = getMessageHeader() + " " + encodedName + "\0";
        System.out.println(message);
        send(message);
	}
}
