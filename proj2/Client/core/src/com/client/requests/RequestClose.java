package com.client.requests;

import com.client.ClientManager;

public class RequestClose extends Request 
{
	public RequestClose() 
	{
		super("CLOSE");
	}
	
	@Override
	public void run()
	{
		String username = ClientManager.getInstance().getUsername();
		String password = ClientManager.getInstance().getPassword();
		
		if (username == null)
			username = "_ignore";
		if (password == null)
			password = "_ignore";
		
		String message = this.type + " " + username + " " + password + "\0";
		send(message);
	}
}
