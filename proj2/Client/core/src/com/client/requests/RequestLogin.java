package com.client.requests;

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
		String message = this.type + " " + username + " " + password + "\0";
		send(message);
	}
}
