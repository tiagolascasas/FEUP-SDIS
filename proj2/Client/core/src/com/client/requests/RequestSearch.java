package com.client.requests;

import java.util.Base64;

public class RequestSearch extends Request
{

	String track;
	public RequestSearch(String track)
	{
		super("SEARCH");
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
