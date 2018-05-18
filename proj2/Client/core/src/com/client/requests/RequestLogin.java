package com.client.requests;

import com.client.ClientManager;

public class RequestLogin extends Request
{
	private String username;
	private String password;

	public RequestLogin(String username, String password)
	{
		super("LOGIN");
		this.username = username;
		this.password = password;
	}
	
	@Override
	public void run()
	{
		if (ClientManager.getInstance().getLoggedIn())
		{
			ClientManager.getInstance().log("You are already logged in");
			return;
		}
		ClientManager.getInstance().setUsername(username);
		ClientManager.getInstance().setPassword(password);
		
		String message = this.type + " " + username + " " + password + "\0";
		send(message);
	}
}
