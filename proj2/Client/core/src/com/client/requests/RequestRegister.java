package com.client.requests;

public class RequestRegister extends Request 
{
	private String username;
	private String password;
	
	public RequestRegister(String username, String password) 
	{
		super("REGISTER");
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
